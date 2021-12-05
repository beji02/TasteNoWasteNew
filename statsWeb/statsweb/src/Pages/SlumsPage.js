import React, {useState} from 'react';
import "./SlumsPage.css"

import Slums from '../Components/Slums/Slums';
import Modal from '../Components/Slums/Modal/Modal.js'

function SlumsPage(){

    const [modalOpen, setModalOpen] = useState(false);

    return(
        <div className = "MainComponent">

            
            {modalOpen ? <Modal setOpenModal={setModalOpen}/> : 
            <>
                <Slums/>
                
            </>}
            
        </div>
        
    )
}

export default SlumsPage;