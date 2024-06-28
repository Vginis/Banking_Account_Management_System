import UserInterface from "./Components/UserInterface"
import Transfer from "./Components/Transfer";
import Deposit from "./Components/Deposit";
import React,{useState} from "react";
import Balance from "./Components/Balance";
import { BrowserRouter as Router, Route, Routes,Navigate } from 'react-router-dom';
import NavigationBar from "./Components/NavigationBar";
import Withdrawal from "./Components/Withdrawal";
import TransactionHistory from "./Components/TransactionHistory";
import Login from "./Components/Login";
import PrivateRoute from "./Components/PrivateRoute";
import Register from "./Components/Register";
function App() {
  const token = localStorage.getItem('token');
  const username = localStorage.getItem('username');
  const showNavBar = !['/login', '/register'].includes(location.pathname);

  return (
    <div>
      {showNavBar && <NavigationBar/>}
      <Router>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register/>}/>
        <Route path="/ui" element={<PrivateRoute><UserInterface currentUser={username} token={token} /></PrivateRoute>} />
        <Route path="/balance" element={<PrivateRoute><Balance currentUser={username} token={token}/></PrivateRoute>} />
        <Route path="/transferFunds" element={<PrivateRoute><Transfer currentUser={username} token={token}/></PrivateRoute>} />
        <Route path="/deposit" element={<Deposit currentUser={username} token={token}/>} />
        <Route path="/withdrawal" element={<Withdrawal currentUser={username} token={token}/>} />
        <Route path="/transactions" element={<TransactionHistory currentUser={username} token={token}/>} />
        <Route path="*" element={<Navigate to="/login" />} />
      </Routes>
    </Router>
    </div>
  )
}

export default App
