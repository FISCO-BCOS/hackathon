from typing import (
    NewType,
    TypeVar,
    Union,
)

Hash32 = NewType('Hash32', bytes)
BlockNumber = NewType('BlockNumber', int)
BlockIdentifier = Union[BlockNumber, Hash32]

Address = NewType('Address', bytes)
HexAddress = NewType('HexAddress', str)
ChecksumAddress = NewType('ChecksumAddress', HexAddress)
AnyAddress = TypeVar('AnyAddress', Address, HexAddress, ChecksumAddress)
