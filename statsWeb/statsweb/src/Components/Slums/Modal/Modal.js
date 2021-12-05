import React from "react";
import "./Modal.css";

const neighorhoodName = "Steaua Fratelia";
const populationNumber = "20.000";
const averageSalary = "700€";
const topSalary = "3500€";
const bottomSalary = "300€";
const homlessPeople = "2%";
const underDeveloped = "10%";
const minPrice = "20.000€"

const neighorhoodNeededPrice = "about 48.000.000€";
const setTargetTime = "2040"

function Modal({ setOpenModal }) {
  return (

    <div className="modalBackground">
      <div className="modalContainer">
        <div className="titleCloseBtn">
          <button
            onClick={() => {
              setOpenModal(false);
            }}
          >
            X
          </button>
        </div>
        <div className="title">
          <h1>{neighorhoodName}</h1>
        </div>
        <div className="body">

          <ul>
            <li>Population: {populationNumber}</li> 
            <li>Average salary: {averageSalary}</li> 
            <li>Top 10% salary: {topSalary} </li> 
            <li>Bottom 10% salary: {bottomSalary}</li>
            <li>Amount of homless people: {homlessPeople}</li>
            <li>People living in underdeveloped areas: {underDeveloped}</li> 
            <li>Minimum price per housing; {minPrice}</li> 
            <li>Estimated price to get this neighborhood back on track: {neighorhoodNeededPrice}</li> 
            <li>This fix should be done by the year: {setTargetTime}</li> 
          </ul>
        </div>
        <div className="footer">
          <button
            onClick={() => {
              setOpenModal(false);
            }}
            id="cancelBtn"
          >
            Cancel
          </button>
        </div>
      </div>
    </div>
  );
}

export default Modal;