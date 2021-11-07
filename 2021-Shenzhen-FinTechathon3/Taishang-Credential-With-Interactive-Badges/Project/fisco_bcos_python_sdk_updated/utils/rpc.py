import logging
import os
import itertools
from eth_utils import (
    to_dict,
    to_text,
    to_bytes,
)
from client.stattool import StatTool
from utils.http import (
    construct_user_agent,
)
from utils.request import (
    make_post_request,
)


from utils.encoding import (
    FriendlyJsonSerde)

from client import clientlogger


def get_default_endpoint():
    return os.environ.get('WEB3_HTTP_PROVIDER_URI', 'http://localhost:8545')


class JSONBaseProvider():
    def __init__(self):
        self.request_counter = itertools.count()

    def decode_rpc_response(self, response):
        text_response = to_text(response)
        return FriendlyJsonSerde().json_decode(text_response)

    def encode_rpc_request(self, method, params):
        rpc_dict = {
            "jsonrpc": "2.0",
            "method": method,
            "params": params or [],
            "id": next(self.request_counter),
        }
        encoded = FriendlyJsonSerde().json_encode(rpc_dict)
        return to_bytes(text=encoded)

    def isConnected(self):
        try:
            response = self.make_request('getClientVersion', [])

            # print(response["result"])
        except IOError:
            return False

        assert response['jsonrpc'] == '2.0'
        assert 'error' not in response

        return True


class HTTPProvider(JSONBaseProvider):
    logger = logging.getLogger("client.providers.HTTPProvider")
    endpoint_uri = None
    _request_args = None
    _request_kwargs = None

    def __init__(self, endpoint_uri=None, request_kwargs=None):
        if endpoint_uri is None:
            self.endpoint_uri = get_default_endpoint()
        else:
            self.endpoint_uri = endpoint_uri
        self._request_kwargs = request_kwargs or {}
        super().__init__()

    def __str__(self):
        return "RPC connection {0}".format(self.endpoint_uri)

    @to_dict
    def get_request_kwargs(self):
        if 'headers' not in self._request_kwargs:
            yield 'headers', self.get_request_headers()
        for key, value in self._request_kwargs.items():
            yield key, value

    def get_request_headers(self):
        return {
            'Content-Type': 'application/json',
            'User-Agent': construct_user_agent(str(type(self))),
        }

    def make_request(self, method, params):

        request_data = self.encode_rpc_request(method, params)
        #print("request", request_data)
        stat = StatTool.begin()
        self.logger.debug("request: %s, %s,data: %s",
                          self.endpoint_uri, method, request_data)

        raw_response = make_post_request(
            self.endpoint_uri,
            method,
            params,
            request_data,
            **self.get_request_kwargs()
        )
        # self.logger.debug("raw response {}, method: {}".format(raw_response, method))
        response = self.decode_rpc_response(raw_response)
        stat.done()
        stat.debug("make_request:{},sendbyts:{}".format(method, len(request_data)))
        self.logger.debug("GetResponse. %s, Response: %s",
                          method, response)
        return response
