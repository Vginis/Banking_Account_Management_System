import Endpoints from './enums';

export async function getBalance(){
    let amount = 0;
    
    try {
        const userResponse = await fetch(Endpoints.USER, {
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
        await Promise.all(userData.accountList.map(async (element) => {
            try {
                const accountResponse = await fetch(`${Endpoints.ACCOUNTS}/${element}`);
                if (!accountResponse.ok) {
                    throw new Error('Bank Account API response was not ok');
                }
                
                const accountData = await accountResponse.json();
                amount = amount + parseFloat(accountData.balance);
            } catch (error) {
                console.error(`Error fetching account ${element}:`, error);
                throw error; // Re-throw the error to stop Promise.all if needed
            }
            
        }));
        return amount;     
    } catch (e) {
        console.error('There was a problem with the fetch operation:', e);
    }
}

export async function makeDeposit(account,amount,token){
    try {
        const response = await fetch(Endpoints.DEPOSITS,
            {
                method:'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify({
                    amount: amount,
                    account: account
                })
            });
        if (!response.ok) {
        throw new Error('API response was not ok ' + response.statusText);
        }
        console.log('Resource updated:');
    } catch (e) {
        console.error('There was a problem with the fetch operation:', e);
    }
}

export async function makeWithdrawal(account,amount,token){
    try {
        const response = await fetch(Endpoints.WITHDRAWALS,
            {
                method:'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify({
                    amount: amount,
                    account: account
                })
        });
        
        if (!response.ok) {
            throw new Error('API response was not ok ' + response.statusText);
        }
        console.log('Resource updated:');
    } catch (e) {
        console.error('There was a problem with the fetch operation:', e);
    }
}