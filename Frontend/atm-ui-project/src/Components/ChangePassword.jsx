import React, { useState } from "react";
import Endpoints from "../util/enums";
function ChangePassword({userData,token}){
    const [oldpassword,setOldPassword] = useState('');
    const [newpassword,setNewPassword] = useState('');
    const [error,setError] = useState(null);
    function handleOldPasswordChange(e){
        setOldPassword(e.target.value);
    }
    
    function handleNewPasswordChange(e){
        setNewPassword(e.target.value);
    }

    function validatePassword(password) {
        const regexPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%&])[A-Za-z\d!@#$%&]{8,20}$/;
        return regexPattern.test(password);
    }

    function handleReset(){
        setOldPassword('');
        setNewPassword('');
    }
    console.log(userData);
    async function handleSubmit(){
        if (!validatePassword(newpassword)) {
            setPasswordError("Invalid Password format. It must be 8-20 characters long and include at least one lowercase letter, one uppercase letter, one number, and one symbol from !@#$%&.");
            return;
        }
        const data = {
            username: userData.username,
            oldPassword: oldpassword,
            newPassword: newpassword
        };
        console.log(token);
        try{
            const response = await fetch(Endpoints.USER+`/changePassword`,{method:'PUT', 
                headers: {
                  'Content-Type': 'application/json',
                  'Authorization': `Bearer ${token}`
              },
                body: JSON.stringify(data)
            })
            if (!response.ok) {
              throw new Error('API response was not ok ' + response.statusText);
           }
            console.log("Password updated")     
            window.alert("Password updated !");
            handleReset();
    } catch(error){
        setError(error);
            console.error('There was a problem with the fetch operation:', error);
            window.alert("There was a problem with the fetch operation!");
    }
    }

    return(<div>
        <h1>Change Your Password</h1>

        <label htmlFor="newPassword">Old Password</label>
        <input type="text" id="newPassword" name="newPassword" value={newpassword} onChange={(e) => handleNewPasswordChange(e)} /><br></br>

        <label htmlFor="oldpassword">New Password</label>
        <input type="text" id="oldpassword" name="oldpassword" value={oldpassword} onChange={(e) => handleOldPasswordChange(e)}/><br></br>

        <p>Submit or Reset</p>
        <button className="button-71" onClick={handleReset}>Reset</button>
        <button className="button-71" onClick={handleSubmit}>Submit</button><br></br>
    </div>);
}

export default ChangePassword;