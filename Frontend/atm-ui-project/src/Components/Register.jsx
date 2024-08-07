import React,{useState} from "react";
import '../styling/Register.css';
import Endpoints from "../util/enums";
import { useNavigate } from 'react-router-dom';
import validator from "validator";
function Register(){
    const [firstName,setFirstName] = useState('');
    const [lastName,setLastName] = useState('');
    const [email,setEmail] = useState('');
    const [username,setUsername] = useState('');
    const [address,setAddress] = useState('');
    const [password,setPassword] = useState('');
    const [error,setError] = useState(null);
    const [passwordError, setPasswordError] = useState(null);

    const navigate = useNavigate(); 

    function handleReset(){
        setFirstName('');
        setLastName('');
        setEmail('');
        setAddress('');
        setPassword('');
        setUsername('');
        setPasswordError('');
    }

    function validatePassword(password) {
        const regexPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%&])[A-Za-z\d!@#$%&]{8,20}$/;
        return regexPattern.test(password);
    }

    async function handleSubmit(){
        if (!validatePassword(password)) {
            setPasswordError("Invalid Password format. It must be 8-20 characters long and include at least one lowercase letter, one uppercase letter, one number, and one symbol from !@#$%&.");
            return;
        }
        const data = {
            userId : 1200000,
            firstName: firstName,
            lastName: lastName,
            username: username,
            email: email,
            address: address,
            accountList: []
        };
        try{
            const response = await fetch(Endpoints.USER+`/new/${password}`,{method:'POST', 
                headers: {
                  'Content-Type': 'application/json',
              },
                body: JSON.stringify(data)
            })
            if (!response.ok) {
              throw new Error('API response was not ok ' + response.statusText);
           }
            console.log("user created")     
            window.alert("User Created !");
            handleReset();
    } catch(error){
        setError(error);
            console.error('There was a problem with the fetch operation:', error);
            window.alert("There was a problem with the fetch operation!");
    }
}

    function handleFirstNameChange(e){
        setFirstName(e.target.value);
    }

    function handleLastNameChange(e){
        setLastName(e.target.value);
    }
    function handleEmailChange(e){
        setEmail(e.target.value);
    }

    function handleAddressChange(e){
        setAddress(e.target.value);
    }

    function handleUsernameChange(e){
        setUsername(e.target.value);
    }

    function handlePasswordChange(e){
        setPassword(e.target.value);
    }

    return(<div className="registration-container">
        <p className="title">Registration Form</p>
        <div className="stringInput">
            <label htmlFor="fname">First Name: <span className="required">*</span></label>
            <input type="text" id="fname" name="fname" placeholder="Enter First Name" required
                value={firstName} onChange={(e) => handleFirstNameChange(e)}></input><br/>

            <label htmlFor="lname">Last Name: <span className="required">*</span></label>
            <input type="text" id="lname" name="lname" placeholder="Enter Last Name" required
                value={lastName} onChange={(e) => handleLastNameChange(e)}></input><br/>
            
            <label htmlFor="username">Username: <span className="required">*</span></label>
            <input type="text" id="username" name="username" placeholder="Enter Username" required
                value={username} onChange={(e) => handleUsernameChange(e)}></input><br/>
            
            <label htmlFor="password">Password: <span className="required">*</span></label>
            <input type="password" id="password" name="password" placeholder="Enter password" required
                value={password} onChange={(e) => handlePasswordChange(e)} ></input><br/>
                {passwordError && <p className="error">{passwordError}</p>}

            <label htmlFor="email">Email: <span className="required">*</span></label>
            <input type="text" id="email" name="email" placeholder="Enter Email" required
                value={email} onChange={(e) => handleEmailChange(e)}></input><br/>

            <label htmlFor="address">Address: <span className="required">*</span></label>
            <input type="text" id="address" name="address" placeholder="e.g. Pattision 17 18956" required
                value={address} onChange={(e) => handleAddressChange(e)}></input><br/>
        </div>

        <p>Submit or Reset</p>
        <button className="button-71" onClick={handleReset}>Reset</button>
        <button className="button-71" onClick={handleSubmit}>Submit</button><br></br>
        <a href="#" onClick={() => {navigate('/login')}}>Back to Login Page</a>
    </div>);
}

export default Register;