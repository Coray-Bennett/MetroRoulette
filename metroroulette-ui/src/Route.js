import './Modal.css';
import { Modal, Button } from "react-bootstrap";


const Route = ({path, visible, handleClose}) => {

    return (
        <>
        <Modal className="modal" show={visible} onHide={handleClose}>
            <Modal.Header>
            <Modal.Title>ROUTE</Modal.Title>
            </Modal.Header>
            <Modal.Body className="modal-body">
                {path.map((value, key) => <p key={key}>{value}</p>)}
            </Modal.Body>
            <Modal.Footer>
            <Button className="close" variant="primary" onClick={handleClose}>
                <span className="mif-cancel"></span>
            </Button>
            </Modal.Footer>
            

        </Modal>
        </>
    )
}

export default Route;