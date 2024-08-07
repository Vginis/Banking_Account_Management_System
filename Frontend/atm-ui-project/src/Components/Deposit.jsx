import React,{useState,useEffect} from "react";
import { useNavigate } from 'react-router-dom';
import "../styling/Deposit.css";
import {makeADeposit} from '../util/functionality.js';
function Deposit({currentUser,token,accounts}){
    const [sourceAccount,setSourceAccount] = useState(null);
    const [amount,setAmount] = useState(0);
    const navigate = useNavigate();
    
    async function makeDeposit(){
        await makeADeposit(sourceAccount,amount,token);

        setAmount(0);
        setSourceAccount(0);
    }

    return (
        <div className='deposits'>
          <h1>Deposit Amount from an Account</h1>

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

          <button className='deposit-button' onClick={makeDeposit}>Deposit</button>
          <button className='back-button' onClick={() => navigate('/ui')}>Back to Home Page</button>

          
        </div>
      );
}
export default Deposit;