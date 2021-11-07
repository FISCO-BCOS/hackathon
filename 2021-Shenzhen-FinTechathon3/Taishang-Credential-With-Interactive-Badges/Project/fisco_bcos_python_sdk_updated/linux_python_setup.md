
pyenv是管理多个python版本的工具。

而pyenv-virtualenv则是pyenv的插件，它支持管理多个virtualenv(pyenv管理不同的python版本，virtualenv则管理不同的依赖库环境)

可能遇到的一些坑：

	可能需要一个新的包libffi-devel，安装此包之后再次进行编译安装即可。

	yum install libffi-devel -y
	(如果是采用apt管理，即将yum换为apt-get即可，下同)

	opencv 版本旧了,按指引安装新的

	tensorflow版本旧了，按指引安装新的

	libgmp3-dev手动安装 https://gmplib.org/

	缺mpfr.h 手动安装 https://www.mpfr.org/mpfr-current/mpfr.html

	
1.pyenv的安装
git clone https://github.com/yyuu/pyenv.git ~/.pyenv

2.将PYENV_ROOT和pyenv init加入bash的~/.bashrc

	echo 'export PATH=~/.pyenv/bin:$PATH' >> ~/.bashrc

	echo 'export PYENV_ROOT=~/.pyenv' >> ~/.bashrc

	echo 'eval "$(pyenv init -)"' >> ~/.bashrc

	source ~/.bashrc

3.需要的依赖关系

	sudo yum install -y build-essential zlib1g-dev libssl-dev
	sudo yum install libsqlite3-dev libbz2-dev libreadline-dev 
	
4.pyenv的一些命令
	
查看可安装的列表
	
	pyenv install --list 

安装指定版本
	
	pyenv install 3.7.3 -v 

	pyenv rehash 
	
设置全局python版本
	
	pyenv global 3.7.3

查看安装版本

	pyenv versions 

卸载指定的python

	pyenv unstall 3.7.3
	
指定shell的python版本
	
	pyenv shell 3.5.1 
	

-------------------------------------------------------------------------------------
	
pyenv-virtualenv则是pyenv的插件，它支持管理多个virtualenv(pyenv管理不同的python版本，virtualenv则管理不同的依赖库环境)



安装pyenv-virtualenv

	git clone https://github.com/yyuu/pyenv-virtualenv.git ~/.pyenv/plugins/pyenv-virtualenv

	echo 'eval "$(pyenv virtualenv-init -)"' >> ~/.bash_profile
	

创建virtualenv
	
	pyenv virtualenv python版本号 虚拟环境名称 
	
	如：
	
	pyenv virtualenv 3.7.3 blc
	
删除virtualenv

	pyenv uninstall 虚拟环境名称

列表virtualenv

  pyenv virtualenvs 

激活/禁用virtualenv

	pyenv activate blc
	
	
	pyenv deactivate 

