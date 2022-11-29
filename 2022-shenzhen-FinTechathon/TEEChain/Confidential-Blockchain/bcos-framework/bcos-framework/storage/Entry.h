#pragma once

#include "Common.h"
#include "bcos-crypto/interfaces/crypto/Hash.h"
#include <bcos-utilities/Common.h>
#include <bcos-utilities/Error.h>
#include <boost/archive/basic_archive.hpp>
#include <boost/exception/diagnostic_information.hpp>
#include <boost/iostreams/device/back_inserter.hpp>
#include <boost/iostreams/stream.hpp>
#include <boost/throw_exception.hpp>
#include <algorithm>
#include <compare>
#include <cstdint>
#include <exception>
#include <initializer_list>
#include <type_traits>
#include <variant>

namespace bcos::storage
{
class Entry
{
public:
    enum Status : int8_t
    {
        NORMAL = 0,
        DELETED = 1,
        EMPTY = 2,
        MODIFIED = 3,  // dirty() can use status
    };

    constexpr static int32_t SMALL_SIZE = 32;
    constexpr static int32_t MEDIUM_SIZE = 64;
    constexpr static int32_t LARGE_SIZE = INT32_MAX;

    constexpr static int32_t ARCHIVE_FLAG =
        boost::archive::no_header | boost::archive::no_codecvt | boost::archive::no_tracking;

    using SBOBuffer = std::array<char, SMALL_SIZE>;

    using ValueType = std::variant<SBOBuffer, std::string, std::vector<unsigned char>,
        std::vector<char>, std::shared_ptr<std::string>,
        std::shared_ptr<std::vector<unsigned char>>, std::shared_ptr<std::vector<char>>>;

    Entry() = default;

    explicit Entry(TableInfo::ConstPtr) {}

    Entry(const Entry&) = default;
    Entry(Entry&&) noexcept = default;
    bcos::storage::Entry& operator=(const Entry&) = default;
    bcos::storage::Entry& operator=(Entry&&) noexcept = default;
    // auto operator<=>(const Entry&) const = default;

    ~Entry() noexcept {}

    template <typename Out, typename InputArchive = boost::archive::binary_iarchive,
        int flag = ARCHIVE_FLAG>
    void getObject(Out& out) const
    {
        auto view = get();
        boost::iostreams::stream<boost::iostreams::array_source> inputStream(
            view.data(), view.size());
        InputArchive archive(inputStream, flag);

        archive >> out;
    }

    template <typename Out, typename InputArchive = boost::archive::binary_iarchive,
        int flag = ARCHIVE_FLAG>
    Out getObject() const
    {
        Out out;
        getObject<Out, InputArchive, flag>(out);

        return out;
    }

    template <typename In, typename OutputArchive = boost::archive::binary_oarchive,
        int flag = ARCHIVE_FLAG>
    void setObject(const In& in)
    {
        std::string value;
        boost::iostreams::stream<boost::iostreams::back_insert_device<std::string>> outputStream(
            value);
        OutputArchive archive(outputStream, flag);

        archive << in;
        outputStream.flush();

        setField(0, std::move(value));
    }

    std::string_view get() const { return outputValueView(m_value); }

    std::string_view getField(size_t index) const
    {
        if (index > 0)
        {
            BOOST_THROW_EXCEPTION(
                BCOS_ERROR(-1, "Get field index: " + boost::lexical_cast<std::string>(index) +
                                   " failed, index out of range"));
        }

        return get();
    }

    template <typename T>
    void setField(size_t index, T&& input)
    {
        if (index > 0)
        {
            BOOST_THROW_EXCEPTION(
                BCOS_ERROR(-1, "Set field index: " + boost::lexical_cast<std::string>(index) +
                                   " failed, index out of range"));
        }

        set(std::forward<T>(input));
    }

    void set(const char* p)
    {
        auto view = std::string_view(p, strlen(p));
        m_size = view.size();
        if (view.size() <= SMALL_SIZE)
        {
            if (m_value.index() != 0)
            {
                m_value = SBOBuffer();
            }

            std::copy_n(view.data(), view.size(), std::get<0>(m_value).data());
            m_status = MODIFIED;
            // m_dirty = true;
        }
        else
        {
            set(std::string(view));
        }
    }

    template <typename Input>
    void set(Input value)
    {
        auto view = inputValueView(value);
        m_size = view.size();
        if (m_size <= SMALL_SIZE)
        {
            if (m_value.index() != 0)
            {
                m_value = SBOBuffer();
            }

            std::copy_n(view.data(), view.size(), std::get<0>(m_value).data());
        }
        else if (m_size <= MEDIUM_SIZE)
        {
            m_value = std::move(value);
        }
        else
        {
            m_value = std::make_shared<Input>(std::move(value));
        }
        m_status = MODIFIED;
        // m_dirty = true;
    }

    template <typename T>
    void setPointer(std::shared_ptr<T>&& value)
    {
        m_size = value->size();
        m_value = value;
    }

    Status status() const { return m_status; }

    void setStatus(Status status)
    {
        m_status = status;
        if (m_status == DELETED)
        {
            m_value = std::string();
        }
        // m_dirty = true;
    }

    bool dirty() const
    {
        return (m_status == MODIFIED || m_status == DELETED);
        // return m_dirty;
    }
    // void setDirty(bool dirty)
    // {
    //     if(dirty)
    //     {
    //         m_status = MODIFIED;
    //     }
    //     else
    //     {
    //         m_status = NORMAL;
    //     }
    //     // m_dirty = dirty;
    // }

    int32_t size() const { return m_size; }

    template <typename Input>
    void importFields(std::initializer_list<Input> values)
    {
        if (values.size() != 1)
        {
            BOOST_THROW_EXCEPTION(
                BCOS_ERROR(StorageError::UnknownEntryType, "Import fields not equal to 1"));
        }

        setField(0, std::move(*values.begin()));
    }

    auto&& exportFields()
    {
        m_size = 0;
        return std::move(m_value);
    }

    bool valid() const { return m_status == Status::NORMAL; }
    crypto::HashType hash(
        std::string_view table, std::string_view key, const bcos::crypto::Hash::Ptr& hashImpl) const
    {
        bcos::crypto::HashType entryHash(0);
        if (m_status == Entry::MODIFIED)
        {
            auto value = get();
            bcos::bytesConstRef ref((const bcos::byte*)value.data(), value.size());
            entryHash = hashImpl->hash(ref);
            if (c_fileLogLevel >= TRACE)
            {
                STORAGE_LOG(TRACE)
                    << "Entry Calc hash, dirty entry: " << table << " | " << toHex(key) << " | "
                    << toHex(value) << LOG_KV("hash", entryHash.abridged());
            }
        }
        else if (m_status == Entry::DELETED)
        {
            entryHash = bcos::crypto::HashType(0x1);
            if (c_fileLogLevel >= TRACE)
            {
                STORAGE_LOG(TRACE) << "Entry Calc hash, deleted entry: " << table << " | "
                                   << toHex(key) << LOG_KV("hash", entryHash.abridged());
            }
        }
        else
        {
            STORAGE_LOG(DEBUG) << "Entry Calc hash, clean entry: " << table << " | " << toHex(key)
                               << " | " << (int)m_status;
        }
        return entryHash;
    }

private:
    std::string_view outputValueView(const ValueType& value) const
    {
        std::string_view view;
        std::visit(
            [this, &view](auto&& valueInside) {
                auto viewRaw = inputValueView(valueInside);
                view = std::string_view(viewRaw.data(), m_size);
            },
            value);
        return view;
    }

    template <typename T>
    std::string_view inputValueView(const T& value) const
    {
        std::string_view view((const char*)value.data(), value.size());
        return view;
    }

    template <typename T>
    std::string_view inputValueView(const std::shared_ptr<T>& value) const
    {
        std::string_view view((const char*)value->data(), value->size());
        return view;
    }

    ValueType m_value;                // should serialization
    int32_t m_size = 0;               // no need to serialization
    Status m_status = Status::EMPTY;  // should serialization
    // bool m_dirty = false;              // no need to serialization
};

}  // namespace bcos::storage

namespace boost::serialization
{
template <typename Archive, typename... Types>
void serialize(Archive& ar, std::tuple<Types...>& t, const unsigned int)
{
    std::apply([&](auto&... element) { ((ar & element), ...); }, t);
}
}  // namespace boost::serialization