import json
from dataclasses import dataclass

from config import storage_file

@dataclass
class User():
    is_login: bool = False
    entity_type: str = "" # type: agency or enterprise
    username: str = ""
    password: str = ""

    def logout(self):
        self.is_login = False
        self.username = ""
        self.password = ""
        self.entity_type = ""
        self.dump()

    def dump(self):
        dump_dict = {
            "username": self.username,
            "password": self.password,
            "entity_type": self.entity_type,
            "is_login": self.is_login
        }

        with open(storage_file, "w", encoding='utf8') as dump_f:
            json.dump(dump_dict, dump_f, ensure_ascii=False)

    def try_load(self):
        try:
            with open(storage_file,'r') as load_f:
                load_dict = json.load(load_f)
            if load_dict is None:
                self.is_login = False
                return

            self.username = load_dict.get("username", "")
            self.password = load_dict.get("password", "")
            self.entity_type = load_dict.get("entity_type", "")
            self.is_login = load_dict.get("is_login", True)
        except:
            self.is_login = False
            return