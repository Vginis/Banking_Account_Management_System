import React, { useState, useEffect } from "react";
import Endpoints from "../util/enums.js";
import '../styling/UserInterface.css';
import { useNavigate } from 'react-router-dom';
function UserInterface({currentUser,token}) {
    const [userData, setUser] = useState(null);
    const [error, setError] = useState(null);
    //todo register 
    const navigate = useNavigate();
    useEffect(() => {
        const fetchData = async () => {
    
            console.log(currentUser);
            try {
                const userResponse = await fetch(Endpoints.USER+`/name/${currentUser}`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${token}`
                    }
                });
                if (!userResponse.ok) {
                    throw new Error('Bank API response was not ok');
                }

                const userData = await userResponse.json();
               
                console.log(userData);
                setUser(userData);
            } catch (error) {
                setError(error);
                console.error('There was a problem with the fetch operation:', error);
            }
        };

        fetchData(); 
    }, []);

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
