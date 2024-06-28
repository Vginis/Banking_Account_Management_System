import React,{useState,useEffect} from "react";
import { useNavigate } from 'react-router-dom';
import Endpoints from "../util/enums";
import "../styling/Deposit.css";
import {makeAWithdrawal} from '../util/functionality.js';
function Withdrawal({currentUser,token}){
    const [sourceAccount,setSourceAccount] = useState(null);
    const [amount,setAmount] = useState(0);
    const [accounts,setAccounts] = useState([]);
    const navigate = useNavigate();
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
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
                const userData = await userResponse.json()
                const extractedAccounts = userData.accountList.map(element => element);
                setAccounts(extractedAccounts);
            } catch (error) {
                setError(error);
                console.error('There was a problem with the fetch operation:', error);
            }
        };
        fetchData();
    }, []);

    async function makeWithdrawal(){
        await makeAWithdrawal(sourceAccount,amount,token);

        setAmount(0);
        setSourceAccount(0);
    }

    return (
        <div className='deposits'>
          <h1>Withdrawal Amount from an Account</h1>

          <label htmlFor="sourceAccount">Select Account: <span className="required">*</span></label>
            <select
            id="sourceAccount"
            name="sourceAccount"
            value={sourceAccount}
            onChange={(e) => setSourceAccount(e.target.value)}
            required>
            <option value="sourceAccount">Select from your accounts...</option>
            {accounts.map((account) => (
                <option key={account} value={account}>
                    Account ID: {account}
                </option>
            ))}
</select><br></br>

          <label htmlFor="amount">Select Amount: <span className="required">*</span></label>
          <input className="amount" type="number" id="amount" name="amount" placeholder="Enter the amount you want to send" required
          value={amount} onChange={(e) => setAmount(e.target.value)}></input><br></br>

          <button className='deposit-button' onClick={makeWithdrawal}>Withdrawal</button>
          <button className='back-button' onClick={() => navigate('/ui')}>Back to Home Page</button>

          
        </div>
      );
}
export default Withdrawal;