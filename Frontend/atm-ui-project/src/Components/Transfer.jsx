import React,{useState,useEffect} from 'react'
import '../styling/Transfer.css';
import Endpoints from '../util/enums';
import { useNavigate } from 'react-router-dom';

function Transfer({currentUser,token}){
  const [sourceAccount,setSourceAccount] = useState(0);
  const [destAccount,setDestAccount] = useState(0);
  const [userAccounts, setUserAccounts] = useState([]);
  const [allAccounts, setAllAccounts] = useState([]);
  const [amount,setAmount] = useState(0);
  const [error, setError] = useState(null);

  const navigate = useNavigate();

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
            const allResponse = await fetch(Endpoints.ACCOUNTS, {
              method: 'GET',
              headers: {
                  'Content-Type': 'application/json',
                  'Authorization': `Bearer ${token}`
              }
          });
            if (!userResponse.ok || !allResponse.ok) {
                throw new Error('Bank API response was not ok');
            }
            const userData = await userResponse.json()
            const extractedAccounts = userData.accountList.map(element => element);
            setUserAccounts(extractedAccounts);

            const allData = await allResponse.json()
            const extractedAccounts2 = allData.map(element => element.accountNumber);
            
            setAllAccounts(extractedAccounts2);
        } catch (error) {
            setError(error);
            console.error('There was a problem with the fetch operation:', error);
        }
    };
    fetchData();
}, []);

  async function makeTransfer(){
    if(amount<=0 || amount>1000){
      throw new Error('Invalid Amount!');
    }
    try {
      const response = await fetch(Endpoints.ACCOUNTS+`/transfer/${sourceAccount}?to=${destAccount}&amount=${amount}`,{method:'PUT', 
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
      }})
      if (!response.ok) {
      throw new Error('API response was not ok ' + response.statusText);
      }
      console.log('Resource updated:');
  } catch (e) {
      console.error('There was a problem with the fetch operation:', e);
  }

    setAmount(0);
    setSourceAccount(0);
    setDestAccount(0);
  }
    return (
        <div className='forms'>
          <h1>Money Transfer from one Account to Another</h1>

          <label htmlFor="sourceAccount">Select Account: <span className="required">*</span></label>
            <select
            id="sourceAccount"
            name="sourceAccount"
            value={sourceAccount}
            onChange={(e) => setSourceAccount(e.target.value)}
            required>
            <option value="sourceAccount">Select from your accounts...</option>
            {userAccounts.map((account) => (
                <option key={account} value={account}>
                    Account ID: {account}
                </option>
            ))}
          </select><br></br>

          <label htmlFor="destAccount">Select Destination Account: <span className="required">*</span></label>
            <select
            id="destAccount"
            name="destAccount"
            value={destAccount}
            onChange={(e) => setDestAccount(e.target.value)}
            required>
            <option value="destAccount">Select from accounts...</option>
            {allAccounts.map((account) => (
                <option key={account} value={account}>
                    Account ID: {account}
                </option>
            ))}
          </select><br></br>

          <label className="amount" htmlFor="amount">Select Amount: <span className="required">*</span></label>
          <input className="amount" type="number" id="amount" name="amount" placeholder="Enter the amount you want to send" required
          value={amount} onChange={(e) => setAmount(e.target.value)}></input><br></br>

          <button className='payment-button' onClick={makeTransfer}>Payment</button>
          <button className='back-button' onClick={() => navigate('/ui')}>Back to Home Page</button>
        </div>
      );
}

export default Transfer;