 
Ŀ¼
 
1\. �˻���Լ
 
---
 
**1\. ���������˻�**
###### Ϊ�û���������Ψһ�˻�

###### URL
> [http://localhost:10100/account/insert](www.api.com/index.php)
 
###### ֧�ָ�ʽ
> JSON
 
###### HTTP����ʽ
> post
 
###### �������

| ����       | ��ѡ | ����     | ˵��         |
| ---------- | ---- | -------- | ------------ |
| user_id    | ture | string   | �û�id       |
| user_money | ture | *big.Int | �û��˻���� |
| user_name  | ture | string   | �û�����     |
| user_icon  | ture | string   | �û�ͷ���ַ |
|            |      |          |              |


###### �����ֶ�
|  �����ֶ�    | ����     | ˵��         |
| ---------- | ---- | -------- |
|tx   |string    |���ϵ�ַ   |

---
 
**2\. ��ѯ�����˻�**
###### Ϊ�û���������Ψһ�˻�

###### URL
> [http://localhost:10100/account/select](www.api.com/index.php)
 
###### ֧�ָ�ʽ
> JSON
 
###### HTTP����ʽ
> post
 
###### �������
|  �����ֶ�    | ����     | ˵��         |
| ---------- | ---- | -------- |                             |
|user_id    |ture    |string|�û�id             |

###### �����ֶ�
|  �����ֶ�    | ����     | ˵��         |
| ---------- | ---- | -------- |
|tx   |string    |���ϵ�ַ   |
|user_money    |true    |*big.Int   |�û��˻����|
|user_name    |ture    |string|�û�����  |
|user_icon    |ture    |string|�û�ͷ���ַ |

---
**3\. �����˻�����**
###### Ϊ�û���������Ψһ�˻�

###### URL
> [http://localhost:10100/account/transfer](www.api.com/index.php)
 
###### ֧�ָ�ʽ
> JSON
 
###### HTTP����ʽ
> post
 
###### �������

| ����       | ��ѡ | ����     | ˵��         |
| ---------- | ---- | -------- | ------------ |
| user_id1    | ture | string   | ������id       |
| user_id2    | ture | string   | ������id       |
| user_money | ture | *big.Int | ���׽�� |
|            |      |          |              |


###### �����ֶ�
|  �����ֶ�    | ����     | ˵��         |
| ---------- | ---- | -------- |
|tx   |string    |���Ͻ��׵�ַ   |

---
2\. �����ʶ��Ȩ��Լ
---
**1\. ��Ȩ����**
###### Ϊ�Ļ���Ʒ����Ψһ��ʶ����������������Ϣ

###### URL
> [http://localhost:10100/collection/insert](www.api.com/index.php)
 
###### ֧�ָ�ʽ
> JSON
 
###### HTTP����ʽ
> post
 
###### �������

| ����       | ��ѡ | ����     | ˵��         |
| ---------- | ---- | -------- | ------------ |
| collection_id  | ture | string   | ��Ȩid|
| collection_name  | ture | string   | �Ļ���Ʒ����       |
| owner_id | ture | string| ��Ʒ������ |
| certificate_time  | ture | string   |     ��Ȩ��֤ʱ��   |
| certificate_organization  | ture | string   | ��Ȩ��֤����       |
| collection_semantic | ture | *big.Int | ��Ʒ���� |
|            |      |          |              |


###### �����ֶ�
|  �����ֶ�    | ����     | ˵��         |
| ---------- | ---- | -------- |
|tx   |string    |���Ͻ��׵�ַ   |

---


**2\. ��Ȩ��ѯ**
###### ��ѯ�Ļ���Ʒ��Ȩ��ʶ��������Ϣ

###### URL
> [http://localhost:10100/collection/select](www.api.com/index.php)
 
###### ֧�ָ�ʽ
> JSON
 
###### HTTP����ʽ
> post
 
###### �������

| ����       | ��ѡ | ����     | ˵��         |
| ---------- | ---- | -------- | ------------ |
| collection_id  | ture | string   | ��Ȩid|

|            |      |          |              |


###### �����ֶ�
|  �����ֶ�    | ����     | ˵��         |
| ---------- | ---- | -------- |
| collection_name  | ture | string   | �Ļ���Ʒ����       |
| owner_id | ture | string| ��Ʒ������ |
| certificate_time  | ture | string   |     ��Ȩ��֤ʱ��   |
| certificate_organization  | ture | string   | ��Ȩ��֤����       |
| collection_semantic | ture | *big.Int | ��Ʒ���� |

---
**3\. ��Ȩ����**
###### �����Ļ���Ʒ��Ȩ��ʶ��Ϣ

###### URL
> [http://localhost:10100/collection/update](www.api.com/index.php)
 
###### ֧�ָ�ʽ
> JSON
 
###### HTTP����ʽ
> post
 
###### �������

| ����       | ��ѡ | ����     | ˵��         |
| ---------- | ---- | -------- | ------------ |
| collection_id  | ture | string   | ��Ȩid|
| collection_name  | ture | string   | �Ļ���Ʒ����       |
| owner_id | ture | string| ��Ʒ������ |
| certificate_time  | ture | string   |     ��Ȩ��֤ʱ��   |
| certificate_organization  | ture | string   | ��Ȩ��֤����       |
| collection_semantic | ture | *big.Int | ��Ʒ���� |
|            |      |          |              |


###### �����ֶ�
|  �����ֶ�    | ����     | ˵��         |
| ---------- | ---- | -------- |
|tx   |string    |���Ͻ��׵�ַ   |

---

**4\. ��Ʒ���ף���Ȩ����**
###### ��ѯ�Ļ���Ʒ��Ȩ��ʶ��������Ϣ
 
###### URL
> [http://localhost:10100/collection/transfer](www.api.com/index.php)
 
###### ֧�ָ�ʽ
> JSON
 
###### HTTP����ʽ
> post
 
###### �������

| ����       | ��ѡ | ����     | ˵��         |
| ---------- | ---- | -------- | ------------ |
| user_id1  | ture | string   | ������id|
| user_id2  | ture | string   | ������id|
| collection_id  | ture | string   | ��Ʒ��Ȩid|
| goods  | ture | string   | ���׽�� |
|            |      |          |              |


###### �����ֶ�
|  �����ֶ�    | ����     | ˵��         |
| ---------- | ---- | -------- |                        |
|:-----   |:------|:-----------------------------   |
|tx   |string    |���Ͻ��׵�ַ   |
---

**5\. AIGC��Ȩ������Ʒ����**
###### AIGC��Ʒ���ɹ����У����ģ�Ͳο�����Ʒ�����߽����������
 
###### URL
> [http://localhost:10100/collection/gincome](www.api.com/index.php)
 
###### ֧�ָ�ʽ
> JSON
 
###### HTTP����ʽ
> post
 
###### �������

| ����       | ��ѡ | ����     | ˵��         |
| ---------- | ---- | -------- | ------------ |
| user_id1  | ture | string   | AIGC��Ʒ������id|
| user_id2  | ture | string   | ����������˵��˻�id|
| goods  | ture | string   | ���˽�� |
|            |      |          |              |


###### �����ֶ�
|  �����ֶ�    | ����     | ˵��         |
| ---------- | ---- | -------- |
|tx   |string    |���Ͻ��׵�ַ   |
|            |      |          | 
---