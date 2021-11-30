# Weidentity-Python-SDK
Weidentity-Python-SDK
## 支持版本
Python3.6+

## How to use

1. 下载weidentity python sdk
```bash
pip install pyweidentity
```

2. 托管模式示例
```python
from pyweidentity.weidentityService import weidentityService

URL = "http://192.168.80.144:6001"
# WeIdentity RestService URL

weid = weidentityService(URL)
create_weid = weid.create_weidentity_did()
print(create_weid)
```


3. 轻客户端模式示例
```python
from pyweidentity.weidentityClient import weidentityClient
import random

URL = "http://192.168.80.144:6001"
# WeIdentity RestService URL

weid = weidentityClient(URL)
privKey = "0xc4a116fb87ae9b8b87842b0f46e1bbf71c37fdae1104fd6d3fd2041e23c8c68e"
nonce = str(random.randint(1, 999999999999999999999999999999))
create_weid = weid.create_weidentity_did(privKey, nonce)
print(create_weid)
```