import Enpoints from './enums';

export async function getBalance(){
    let amount = 0;
    
    try {
        const userResponse = await fetch(Enpoints.USER, {
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
                const accountResponse = await fetch(`${Enpoints.ACCOUNTS}/${element}`);
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

export async function makeADeposit(account,euros,token){
    try {
        const response = await fetch(Enpoints.DEPOSITS+`/make/${account}?amount=${euros}`,{method:'PUT', 
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        });
        if (!response.ok) {
        throw new Error('API response was not ok ' + response.statusText);
        }
        console.log('Resource updated:');
    } catch (e) {
        console.error('There was a problem with the fetch operation:', e);
    }
}

export async function makeAWithdrawal(account,euros,token){
    try {
        const response = await fetch(Enpoints.WITHDRAWALS+`/make/${account}?amount=${euros}`,{method:'PUT', 
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        });
        if (!response.ok) {
        throw new Error('API response was not ok ' + response.statusText);
        }
        console.log('Resource updated:');
    } catch (e) {
        console.error('There was a problem with the fetch operation:', e);
    }
}