import React,{useState} from "react";
import '../styling/Register.css';
import Endpoints from "../util/enums";
import { useNavigate } from 'react-router-dom';
function Register(){
    const [firstName,setFirstName] = useState('');
    const [lastName,setLastName] = useState('');
    const [email,setEmail] = useState('');
    const [username,setUsername] = useState('');
    const [address,setAddress] = useState('');
    const [error,setError] = useState(null);

    const navigate = useNavigate(); 

    function handleReset(){
        setFirstName('');
        setLastName('');
        setEmail('');
        setAddress('');
    }

    async function handleSubmit(){
        const data = {
            userId : 5,
            firstName: firstName,
            lastName: lastName,
            username: username,
            email: email,
            address: address,
            accountList: []
        };
        try{
            const response = await fetch(Endpoints.USER+'/new',{method:'POST', 
                headers: {
                  'Content-Type': 'application/json',
              },
                body: JSON.stringify(data)
            })
            if (!response.ok) {
              throw new Error('API response was not ok ' + response.statusText);
           }
            console.log("user created")     
    } catch(error){
        setError(error);
            console.error('There was a problem with the fetch operation:', error);
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