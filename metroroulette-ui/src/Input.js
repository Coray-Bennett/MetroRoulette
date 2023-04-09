import './Input.css';
import { useState } from 'react';
import LineSelect from './LineSelect.js';
import RedLine from './images/WMATA_Red.svg';
import GreenLine from './images/WMATA_Green.svg';
import OrangeLine from './images/WMATA_Orange.svg';
import BlueLine from './images/WMATA_Blue.svg';
import SilverLine from './images/WMATA_Silver.svg';
import YellowLine from './images/WMATA_Yellow.svg';


const Input = () => {

    const [stationCode, setStationCode] = useState("");

    return (
        <>
        <datalist id="stations"></datalist>
        <div className="station-input">
            <span class="mif-room"></span>
            <input id="search" type="search" list="stations" value={stationCode}
                onChange={(e) => setStationCode(e.target.value)}>
            </input>
        </div>
        
        <div className="lines-input">
            <LineSelect className="line" source={RedLine} desc="Red Line"/>
            <LineSelect className="line" source={GreenLine} desc="Green Line"/>
            <LineSelect className="line" source={OrangeLine} desc="Orange Line"/>
            <LineSelect className="line" source={BlueLine} desc="Blue Line"/>
            <LineSelect className="line" source={SilverLine} desc="Silver Line"/>
            <LineSelect className="line" source={YellowLine} desc="Yellow Line"/>
        </div>
        </>
    )
}

export default Input;

