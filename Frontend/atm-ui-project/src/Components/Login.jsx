import React, { useState } from 'react'
import '../styling/Login.css';
import Endpoints from '../util/enums';
import { useNavigate } from 'react-router-dom';
function Login(){
    const [givenUsername,setGivenUsername] = useState(null);
    const [givenPassword,setGivenPassword] = useState(null);
    const [error, setError] = useState(null);
    const navigate = useNavigate(); 
    let token = '';
    const handleLogin = async () => {

        try{
            const authResponse = await fetch(Endpoints.LOGIN, {
                method: 'POST',
                headers: {
                  'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    username: givenUsername,
                    password: givenPassword,
                  }),
              });
            if (!authResponse.ok){
                throw new Error('Login Failed');
            }

            const data = await authResponse.json();
            token = data.token;
        } catch (error) {
            setError(error);
            console.error('There was a problem with the fetch operation:', error);
        }
        localStorage.setItem('token', token);
        localStorage.setItem('username',givenUsername);

        window.location.href = '/ui';
      }

    return(<div className='login-container'>
        <h1>Login Page</h1>
        <label htmlFor="username">Username</label>
        <input type="text" id="username" name="username" onChange={(e) => setGivenUsername(e.target.value)}/><br></br>
        <label htmlFor="password">Password</label>
        <input type="password" id="password" name="password" onChange={(e) => setGivenPassword(e.target.value)}/><br></br>
        
        <button className="button-71" onClick={handleLogin}> Log In</button><br></br>
        <a href="#" onClick={() => {navigate('/register')}}>Don't have an account? Register here</a>
    </div>)
}

export default Login;
