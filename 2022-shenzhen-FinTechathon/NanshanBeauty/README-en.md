# B-Recycle Battery Recycling Management Platform

## Project Introduction
**B-Recycle** is a blockchain battery recycling management platform based on `FISCO BCOS`. It uses `Vue + SpringBoot` for front-end 
and back-end construction, and integrates the `Java-SDK` of `FISCO BCOS` and `WeEvent`.

The platform is committed to breaking through the information barriers in the upstream and downstream of the power battery 
industry, using blockchain technology to help government departments supervise and promote the sustainable and healthy development of the industry.

## Background Of The Project
In recent years, due to the national demand for energy conservation and emission reduction and the realization of "dual carbon" 
goals, policies have continued to support the development of the new energy vehicle industry. **The sales of new energy vehicles 
have increased year by year and are expected to maintain a strong growth rate in the future**. Correspondingly, the production 
and recycling market of power batteries is also expanding rapidly. This also means that **the amount of batteries retired in 
the future will naturally increase accordingly**. GGII predicts that in 2025, my country's decommissioned power batteries will 
accumulatively reach 137.4GWh, and the output value of cascade utilization and recycling is expected to exceed 100 billion.

![image](https://github.com/cmgun/B-Recycle/blob/main/docs/output/background1.png?raw=true)

![image](https://github.com/cmgun/B-Recycle/blob/main/docs/output/background2.png?raw=true)

**Regulating the battery recycling market is an important prerequisite for the sustainable development of the new energy vehicle market**.
The Ministry of Industry and Information Technology issued the "Industry Standard Conditions for the Comprehensive Utilization
of Waste Power Batteries of New Energy Vehicles". However, it is very difficult to enter the white list, and at least more
than 50 review conditions need to be met, which means that **formal recycling companies need higher costs**. This has led to
a serious phenomenon of "bad money driving out good money" in the battery recycling market. That is, most of the used power
batteries have flowed into the hands of low-cost and irregular recycling workshops, bringing survival pressure of vicious
competition to formal recycling companies, as well as causing chaos in the market order and makes it difficult for the government to supervise.

The non-compliance handling of small workshops will also lead to waste of resources and unfriendly environment. Therefore, 
**how to promote the standardization of power battery recycling through a more sound regulatory system, and to reduce the 
cost of the battery industry chain will become a key link in promoting the promotion of new energy electric vehicles**.

![image](https://github.com/cmgun/B-Recycle/blob/main/docs/output/background3.jpg?raw=true)


## Project Objectives
The **B-Recycle battery recycling management platform** aims at problems in the power battery recycling market such as 
**difficulty in obtaining qualifications for formal power battery recycling companies, high recycling costs, and difficulties in government supervision**, 
witch is based on blockchain technology and innovatively designed **institutional access, incentive mechanism, bidding transaction 
and other platform services**. It effectively solves the above industrial pain points, and provide a fully transparent, 
trustworthy, and easy-to-supervise power battery recycling management platform on the chain, which meets the needs of the 
current development and improvement of the industry, and has practical application value and The feasibility of landing 
can help regulate the power battery recycling market.

## Solution
### 1. Battery recycling blockchain
![image](https://github.com/cmgun/B-Recycle/blob/main/docs/output/solution1.jpg?raw=true)

### 2. Integral calculation model
- Integral initialization
  ![image](https://github.com/cmgun/B-Recycle/blob/main/docs/output/pointmodel1.png?raw=true)
  ![image](https://github.com/cmgun/B-Recycle/blob/main/docs/output/pointmodel2.png?raw=true)

- Point consumption
  ![image](https://github.com/cmgun/B-Recycle/blob/main/docs/output/pointmodel3.png?raw=true)

- Point distribution
  ![image](https://github.com/cmgun/B-Recycle/blob/main/docs/output/pointmodel4.png?raw=true)

### 3. System Architecture
![image](https://github.com/cmgun/B-Recycle/blob/main/docs/output/solution2.jpg?raw=true)

## Project Structure Description
`Contracts` contains our contracts based on solidity, the api of contracts can see:[READMD](Contracts/README.md)

`Projects` is our system design, contains front-end, back-end and documents, more details:[READMD](Projects/README.md)

## Open Source Agreement
We use `Apache-2.0 license` as open source agreement.

## Team Introduce
Here is our team members, welcome for email us if you have any problem.

| Name                   | Responsibility                                                               | Email                       | github                          |
|------------------------|------------------------------------------------------------------------------|-----------------------------|---------------------------------|
| CMGUN(Qilin Chen)      | project coordination, system design, front-end/back-end/contract development | cmgun@foxmail.com           | https://github.com/cmgun        |
| Fiñorita(Meixian Fong) | product design, front-end development                                        | 771348742@qq.com            | https://github.com/Finorita     |
| Morton(Hongyu Liu)     | points system design, contract development                                   | szl95276@163.com            | https://github.com/Littleciciz1 |
| Rui(Xiangrui He)       | industry research, point system design, PPT production                       | 2210515005@email.szu.edu.cn | -                               |
| Fay(Yafei Wu)          | industry research, product design, document writing                          | 452580962@qq.com            | -                               |