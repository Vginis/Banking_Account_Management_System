import {useState} from "react";
import { useNavigate } from 'react-router-dom';
import "../styling/Deposit.css";
import {makeWithdrawal} from '../util/functionality.js';
function Withdrawal({token,accounts}){
    const [sourceAccount,setSourceAccount] = useState(null);
    const [amount,setAmount] = useState(0);
    const navigate = useNavigate();

    async function handleWithDrawal(){
        await makeWithdrawal(sourceAccount,amount,token);

        setAmount(0);
        setSourceAccount(0);
    }

    const hasSelectedValidOptions = amount > 0 && sourceAccount > 0 && amount <=1000

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

          <button className='deposit-button' onClick={handleWithDrawal} disabled={!hasSelectedValidOptions}>Withdrawal</button>
          <button className='back-button' onClick={() => navigate('/ui')}>Back to Home Page</button>

          
        </div>
      );
}
export default Withdrawal;