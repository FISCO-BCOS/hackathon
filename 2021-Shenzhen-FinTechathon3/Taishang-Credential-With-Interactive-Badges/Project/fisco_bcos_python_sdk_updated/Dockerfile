FROM python:3.7-alpine

ENV USER root

ENV PATH /root/.local/bin/:$PATH 

RUN mkdir /python-sdk

WORKDIR /python-sdk

RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.ustc.edu.cn/g' /etc/apk/repositories && \
    apk update && \
    apk add --no-cache gcc g++ python python-dev py-pip openssl bash linux-headers libffi-dev openssl-dev curl wget

COPY requirements.txt /requirements.txt

RUN pip install -r /requirements.txt -i https://pypi.tuna.tsinghua.edu.cn/simple --no-cache-dir

RUN curl -LO https://github.com/FISCO-BCOS/FISCO-BCOS/releases/download/"$(curl -s https://api.github.com/repos/FISCO-BCOS/FISCO-BCOS/releases | grep "\"v2\.[0-9]\.[0-9]\"" | sort -u | tail -n 1 | cut -d \" -f 4)"/build_chain.sh && chmod u+x build_chain.sh && \
    bash build_chain.sh -l "127.0.0.1:4" -p 30300,20200,8545

COPY . /python-sdk

RUN bash init_env.sh -i && \
    cp /python-sdk/nodes/127.0.0.1/sdk/* bin/ && \
    ln -s /root/.local/bin/register-python-argcomplete /bin/register-python-argcomplete && \
    echo "eval \"\$(register-python-argcomplete ./console.py)\"" >> ~/.bashrc && \
    echo "eval \"/python-sdk/nodes/127.0.0.1/start_all.sh\"" >> ~/.bashrc

EXPOSE 20200 30300 8545

CMD ["bash"]
