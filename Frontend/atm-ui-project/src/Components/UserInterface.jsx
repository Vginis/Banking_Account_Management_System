import React, { useState, useEffect } from "react";
import Endpoints from "../util/enums.js";
import '../styling/UserInterface.css';
import { useNavigate } from 'react-router-dom';
function UserInterface({userData}) {
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    if (error) {
        return <div className="context"><h1>Error: {error.message}</h1></div>;
    }

    if (!userData) {
        return <div className="context"><h1>Loading...</h1></div>;
    }

    function handleLogout(){
        localStorage.removeItem('token');
        navigate('/login');
    }

    return (
        <div className="context">
            <h1>Welcome Mr {userData.firstName} {userData.lastName}</h1>

            <button onClick={() => {navigate('/balance')}}>Check Balance</button>
            <button onClick={() => {navigate('/withdrawal')}}>Withdrawal</button>
            <button onClick={() => {navigate('/deposit')}}>Deposit</button>
            <button onClick={() => {navigate('/transferFunds')}}>Transfer to other Account</button>
            <button onClick={() => {navigate('/transactions')}}>Show Transaction History</button>
            <button onClick={handleLogout}> Logout</button><br></br>
        </div>
    );
}

export default UserInterface;
