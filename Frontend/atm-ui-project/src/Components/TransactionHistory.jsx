import React,{useState,useEffect} from "react";
import Endpoints from "../util/enums";
import '../styling/Transactions.css';
function TransactionHistory({currentUser,token,accounts}){

    const [deposits, setDeposits] = useState([]);
    const [withdrawals, setWithdrawals] = useState([]);
    const [error, setError] = useState(null);


    useEffect(() => {
        const fetchData = async () => {
            try {
                const allDeposits = [];
                const allWithdrawals = [];
                for(let i in accounts){
                    const response = await fetch(Endpoints.DEPOSITS+`/account/${accounts[i]}`, {
                      method: 'GET',
                      headers: {
                          'Content-Type': 'application/json',
                          'Authorization': `Bearer ${token}`
                      }
                  });
                    const response2 = await fetch(Endpoints.WITHDRAWALS+`/account/${accounts[i]}`, {
                      method: 'GET',
                      headers: {
                          'Content-Type': 'application/json',
                          'Authorization': `Bearer ${token}`
                      }
                  });
                    if (!response.ok || !response2.ok) {
                        throw new Error(`Failed to fetch deposits or withdrawls for account ${accounts[i]}`);
                    }
                    const depositTransactions = await response.json();
                    allDeposits.push(...depositTransactions);

                    const withdrawalTransactions = await response2.json();
                    allWithdrawals.push(...withdrawalTransactions);
                }
                setDeposits(allDeposits);
                setWithdrawals(allWithdrawals);
            } catch (error) {
                setError(error);
                console.error('There was a problem with the fetch operation:', error);
            }
        };
        fetchData();
    }, [accounts]);

    return(<div className="transaction-history">
        <h1>Transaction History</h1>
    <div className="deposit-history">
    <h2>Deposit History</h2>
        {error && <p className="error">{error}</p>}
        {!error && (
          <table>
            <thead>
              <tr>
                <th>Date</th>
                <th>Amount</th>
                <th>Account</th>
              </tr>
            </thead>
            <tbody>
              {deposits.map((transaction) => (
                <tr key={transaction.id}>
                  <td>{transaction.date}</td>
                  <td>{transaction.amount}</td>
                  <td>{transaction.account}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
      <div className="deposit-history">
    <h2>Withdrawal History</h2>
        {error && <p className="error">{error}</p>}
        {!error && (
          <table>
            <thead>
              <tr>
                <th>Date</th>
                <th>Amount</th>
                <th>Account</th>
              </tr>
            </thead>
            <tbody>
              {withdrawals.map((transaction) => (
                <tr key={transaction.id}>
                  <td>{transaction.date}</td>
                  <td>{transaction.amount}</td>
                  <td>{transaction.account}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div></div>
    );
}
export default TransactionHistory;