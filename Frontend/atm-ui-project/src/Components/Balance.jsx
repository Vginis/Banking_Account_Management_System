import React,{useState,useEffect} from 'react';
import Endpoints from "../util/enums.js";
import "../styling/Balance.css";
function Balance({token,userData,accounts}){
    const [firstName, setFirstName] = useState(null);
    const [error, setError] = useState(null);
    const [accountBalances, setAccountBalances] = useState({});
    useEffect(() => {
        const fetchData = async () => {
            try {
                setFirstName(userData.firstName);
                const balancePromises = accounts.map(account => fetchBalances(account));
                const balances = await Promise.all(balancePromises);
                
                const accountBalanceMap = {};
                accounts.forEach((account, index) => {
                    accountBalanceMap[account] = balances[index];
                });

                setAccountBalances(accountBalanceMap);
            } catch (error) {
                setError(error);
                console.error('There was a problem with the fetch operation:', error);
            }
        };
        fetchData();
    }, [token, userData, accounts]);

    async function fetchBalances(account){
        try {
            const accountResponse = await fetch(Endpoints.ACCOUNTS+`/${account}`,
                {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${token}`
                    }
        });
            if (!accountResponse.ok) {
                throw new Error('Bank Account API response was not ok');
            }

            const accountData = await accountResponse.json();
           
            return accountData.balance;
        } catch (error) {
            setError(error);
            console.error('There was a problem with the fetch account operation:', error);
        }
    }

    function calculateTotalBalance(){
        let total = 0;
        for (const element in accountBalances){
            total += accountBalances[element];
        }
        return total;
    }
    return(<div className="balance">
        <h1>Balance of Accounts for {firstName}</h1>
        <ul>
            {accounts.map((account,index) => 
            <li key={index}>Account Number: {account} amount:{accountBalances[account]}</li>)}
            <li>In total = {calculateTotalBalance()}</li>
        </ul>
        </div>);
}

export default Balance;