
class BcosKeyPair:
    private_key = None
    public_key = None
    address = None

    def getdetail(self, sep="\n"):
        strmsg = "private key: %s%spublic key: %s%saddress: %s" % (
            self.private_key, sep, self.public_key, sep, self.address)
        return strmsg
