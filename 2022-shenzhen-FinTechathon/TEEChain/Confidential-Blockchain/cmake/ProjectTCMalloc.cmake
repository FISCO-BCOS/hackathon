include(ExternalProject)

set(GPERFTOOLS_OPTIONS --disable-shared --disable-cpu-profiler --disable-heap-profiler --disable-heap-checker --disable-debugalloc --enable-minimal)
set(TCMALLOC_LIB_NAME "libtcmalloc_minimal.a")
if (DEBUG)
    set(GPERFTOOLS_OPTIONS "")
    set(TCMALLOC_LIB_NAME "libtcmalloc${CMAKE_STATIC_LIBRARY_SUFFIX}")
endif()

set(TCMALLOC_CONFIG ./configure CXXFLAGS=-DHAVE_POSIX_MEMALIGN_SYMBOL=1 --enable-frame-pointers ${GPERFTOOLS_OPTIONS} --prefix=${CMAKE_SOURCE_DIR}/deps/src/gperftools)

set(TCMALLOC_MAKE make install)

ExternalProject_Add(gperftools
    PREFIX ${CMAKE_CURRENT_SOURCE_DIR}/deps/
    DOWNLOAD_NAME gperftools-2.10.tar.gz
    DOWNLOAD_NO_PROGRESS 1
    URL https://github.com/gperftools/gperftools/releases/download/gperftools-2.10/gperftools-2.10.tar.gz
       # https://osp-1257653870.cos.ap-guangzhou.myqcloud.com/FISCO-BCOS/FISCO-BCOS/deps/gperftools-2.7.tar.gz
       # https://raw.githubusercontent.com/FISCO-BCOS/LargeFiles/master/libs/gperftools-2.7.tar.gz
    URL_HASH SHA256=83e3bfdd28b8bcf53222c3798d4d395d52dadbbae59e8730c4a6d31a9c3732d8
    BUILD_IN_SOURCE 1
    LOG_CONFIGURE 1
    LOG_BUILD 1
    LOG_INSTALL 1
    CONFIGURE_COMMAND ${TCMALLOC_CONFIG}
    BUILD_COMMAND ${TCMALLOC_MAKE}
    INSTALL_COMMAND ""
    BUILD_BYPRODUCTS <SOURCE_DIR>/.libs/${TCMALLOC_LIB_NAME}
)

ExternalProject_Get_Property(gperftools SOURCE_DIR)
add_library(TCMalloc STATIC IMPORTED GLOBAL)

set(TCMALLOC_INCLUDE_DIR ${SOURCE_DIR}/include/)
set(TCMALLOC_LIBRARY ${SOURCE_DIR}/.libs/${TCMALLOC_LIB_NAME})
file(MAKE_DIRECTORY ${TCMALLOC_INCLUDE_DIR})  # Must exist.

set_property(TARGET TCMalloc PROPERTY IMPORTED_LOCATION ${TCMALLOC_LIBRARY})
set_property(TARGET TCMalloc PROPERTY INTERFACE_INCLUDE_DIRECTORIES ${TCMALLOC_INCLUDE_DIR})

add_dependencies(TCMalloc gperftools)
unset(SOURCE_DIR)
