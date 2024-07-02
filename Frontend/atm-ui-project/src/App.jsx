import UserInterface from "./Components/UserInterface"
import Transfer from "./Components/Transfer";
import Deposit from "./Components/Deposit";
import React,{useState,useEffect} from "react";
import Balance from "./Components/Balance";
import { BrowserRouter as Router, Route, Routes,Navigate } from 'react-router-dom';
import NavigationBar from "./Components/NavigationBar";
import Withdrawal from "./Components/Withdrawal";
import TransactionHistory from "./Components/TransactionHistory";
import Login from "./Components/Login";
import PrivateRoute from "./Components/PrivateRoute";
import Register from "./Components/Register";
import Endpoints from "./util/enums";
function App() {
  const token = localStorage.getItem('token');
  const [userData, setUser] = useState(null);
  const username = localStorage.getItem('username');
  const showNavBar = !['/login', '/register'].includes(location.pathname);
  const [accounts,setAccounts] = useState([]);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
        try {
            const userResponse = await fetch(Endpoints.USER+`/name/${username}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                }
            });
            if (!userResponse.ok) {
                throw new Error('Bank API response was not ok');
            }
            const userData = await userResponse.json()
            const extractedAccounts = userData.accountList.map(element => element);
            setAccounts(extractedAccounts);
            setUser(userData);
        } catch (error) {
            setError(error);
            console.error('There was a problem with the fetch operation:', error);
        }
    };
    fetchData();
}, [username,token]);
  return (
    <div>
      {showNavBar && <NavigationBar/>}
      <Router>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register/>}/>
        <Route path="/ui" element={<PrivateRoute><UserInterface userData={userData}/></PrivateRoute>} />
        <Route path="/balance" element={<PrivateRoute><Balance token={token} userData={userData} accounts={accounts}/></PrivateRoute>} />
        <Route path="/transferFunds" element={<PrivateRoute><Transfer currentUser={username} token={token} accounts={accounts}/></PrivateRoute>} />
        <Route path="/deposit" element={<Deposit currentUser={username} token={token} accounts={accounts}/>} />
        <Route path="/withdrawal" element={<Withdrawal currentUser={username} token={token} accounts={accounts}/>} />
        <Route path="/transactions" element={<TransactionHistory currentUser={username} token={token} accounts={accounts}/>} />
        <Route path="*" element={<Navigate to="/login" />} />
      </Routes>
    </Router>
    </div>
  )
}

export default App
