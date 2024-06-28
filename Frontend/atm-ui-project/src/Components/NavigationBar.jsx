import React from "react";
import '../styling/NavigationBar.css';
function NavigationBar(){
    return(<nav className="navbar">
        <ul>
            <li><a href="/ui">Home</a></li>
            <li><a href="#about">About</a></li>
            <li><a href="#services">Services</a></li>
            <li><a href="#contact">Contact</a></li>
        </ul>
    </nav>)
}

export default NavigationBar;