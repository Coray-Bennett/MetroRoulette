import './Info.css';
import './Modal.css';
import { useState, React } from 'react';
import { Modal, Button } from "react-bootstrap";

const Info = () => {
    const [showModal, setShow] = useState(false);
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    return (
        <>
        <Modal className="modal" show={showModal} onHide={handleClose}>
            <Modal.Header>
            <Modal.Title>INFO</Modal.Title>
            </Modal.Header>
            <Modal.Body className="modal-body">
                <ul>
                    <li>
                    <span className="mif-room icon"></span>
                    <p> Current (starting) station</p>
                    </li>
                    <li>
                    <span className="mif-subway icon"></span>
                    <p> Max number of stops on route</p>
                    </li>
                    <li>
                    <span className="mif-watch icon"></span>
                    <p> Max route length in minutes</p>
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