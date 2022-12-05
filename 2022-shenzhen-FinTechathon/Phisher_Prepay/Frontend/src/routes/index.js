import { Navigate } from "react-router-dom";
import  Tabs  from "../components/Tabs";
import Login from "../components/Login";
import Cards from "../components/Cards";
import Messages from "../components/Messages";
import Shops from "../components/Shops";
import PersonalCenter from "../components/PersonalCenter";
import ExpRec from "../components/Cards/ExpRec";
import CardItem from "../components/Cards/CardItem";
import ExpDetail from "../components/Cards/ExpDetail";
import CheckOut from "../components/Cards/CheckOut";
import Appeal from "../components/Cards/Appeal";
import ShopDetail from "../components/Shops/ShopDetail";
import ContractDetail from "../components/Shops/ContractDetail";
import ShowContract from "../components/Cards/ShowContract";

const routes = [
    // {
    //     path:'/',
    //     element:<Navigate to="/tabs"/>,
    // },
    {
        path:'login',
        element:<Login/>,
    },
    {
        path:'/showContract',
        element:<ShowContract/>
    },
    {
        path:'/contractDetail',
        element:<ContractDetail/>,
    },
    {
        path:'/shopDetail',
        element:<ShopDetail/>,
    },
    {
        path:'/expRec',
        element:<ExpRec/>,
    },
    {
        path:'/expDetail',
        element:<ExpDetail/>
    },
    {
        path:'/checkOut',
        element:<CheckOut/>
    },
    {
        path:'appeal',
        element:<Appeal/>
    },
    {
        path:'/tabs',
        element:<Tabs />,
        children:[
            {
                path:'Shops',
                element:<Shops/>,
            },
            {
                path:'Cards',
                element:<Cards/>,
                children:[
                    {
                        path:'CardItem',
                        element:<CardItem/>
                    }
                ]
            },
            {
                path:'messages',
                element:<Messages/>,
            },
            {
                path:'me',
                element:<PersonalCenter />,
            }
        ]
    },
]

export default routes