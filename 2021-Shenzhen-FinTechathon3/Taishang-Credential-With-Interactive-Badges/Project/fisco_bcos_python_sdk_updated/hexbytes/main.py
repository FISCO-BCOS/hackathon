from typing import (
    Type,
    Union,
    cast,
    overload,
)

from eth_utils import (
    hexstr_if_str,
    to_bytes,
)


class HexBytes(bytes):
    """
    HexBytes is a *very* thin wrapper around the python
    built-in :class:`bytes` class. It has these three changes:

    1. Accepts hex strings as an initializing value
    2. Returns hex with prefix '0x' from :meth:`HexBytes.hex`
    3. The string representation at console is in hex
    """
    def __new__(cls: Type[bytes], val: Union[bytes, int, str]) -> "HexBytes":
        bytesval = hexstr_if_str(to_bytes, val)
        return cast(HexBytes, super().__new__(cls, bytesval))  # type: ignore  # https://github.com/python/typeshed/issues/2630  # noqa: E501

    def hex(self) -> str:
        """
        Just like :meth:`bytes.hex`, but prepends "0x"
        """
        return '0x' + super().hex()

    @overload
    def __getitem__(self, key: int) -> int:
        ...

    @overload  # noqa: F811
    def __getitem__(self, key: slice) -> 'HexBytes':
        ...

    def __getitem__(self, key: Union[int, slice]) -> Union[int, bytes, 'HexBytes']:  # noqa: F811
        result = super().__getitem__(key)
        if hasattr(result, 'hex'):
            return type(self)(result)
        else:
            return result

    def __repr__(self) -> str:
        return 'HexBytes(%r)' % self.hex()
