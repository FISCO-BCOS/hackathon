def construct_user_agent(class_name):

    user_agent = 'liteclient.py/{version}/{class_name}'.format(
        version="1.0",
        class_name=class_name,
    )
    return user_agent
