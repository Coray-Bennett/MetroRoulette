import './Info.css';
import { useState } from 'react';
import { Modal, Button } from "react-bootstrap";

const Info = () => {
    const [showModal, setShow] = useState(false);

    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    return (
        <> 

        <Modal className="info-modal" show={showModal} onHide={handleClose}>
            <Modal.Header>
            <Modal.Title>INFO</Modal.Title>
            </Modal.Header>
            <Modal.Body className="modal-body">
                <ul>
                    <li>
                    <span className="mif-room icon"></span>
                    <p>- The station you are currently at</p>
                    </li>
                    <li>
                    <span className="mif-subway icon"></span>
                    <p> - The maximum number of stops for your route</p>
                    </li>
                    <li>
                    <span className="mif-watch icon"></span>
                    <p> - The maximum number of minutes for your route</p>
                    </li>
                </ul> 
            </Modal.Body>
            <Modal.Footer>
            <Button className="close" variant="primary" onClick={handleClose}>
                <span className="mif-cancel"></span>
            </Button>
            </Modal.Footer>
            

        </Modal>

        <button className="info" onClick={handleShow}>
            <span className="mif-info"></span>
        </button>
        </>
    )
}

export default Info;