import './Input.css';
import { useState } from 'react';
import LineSelect from './LineSelect';
import RedLine from './images/WMATA_Red.svg';
import GreenLine from './images/WMATA_Green.svg';
import OrangeLine from './images/WMATA_Orange.svg';
import BlueLine from './images/WMATA_Blue.svg';
import SilverLine from './images/WMATA_Silver.svg';
import YellowLine from './images/WMATA_Yellow.svg';


const Input = () => {

    const [stationCode, setStationCode] = useState("");
    const [selected, setSelected] = useState(Array(6).fill(true));
    const [lines, setLines] = useState(new Set(["RD", "GR", "OR", "BL", "SV", "YL"]));

    function updateLines(index, line) {
        let newSelected = selected;
        newSelected[index] = !newSelected[index];
        setSelected(newSelected);

        let newLines = lines;
        if(newLines.has(line)) {
            newLines.delete(line);
        }
        else {
            newLines.add(line);
        }
        setLines(newLines);
    }

    return (
        <>
        <datalist id="stations"></datalist>
        <div className="station-input">
            <span className="mif-room"></span>
            <input id="search" type="search" list="stations" value={stationCode}
                onChange={(e) => setStationCode(e.target.value)}>
            </input>
        </div>
        
        <div className="lines-input">
            <LineSelect selected={selected[0]} className="line" source={RedLine} desc="Red Line" onClick={() => updateLines(0, "RD")}/>
            <LineSelect selected={selected[1]} className="line" source={GreenLine} desc="Green Line" onClick={() => updateLines(1, "GR")}/>
            <LineSelect selected={selected[2]} className="line" source={OrangeLine} desc="Orange Line" onClick={() => updateLines(2, "OR")}/>
            <LineSelect selected={selected[3]} className="line" source={BlueLine} desc="Blue Line" onClick={() => updateLines(3, "BL")}/>
            <LineSelect selected={selected[4]} className="line" source={SilverLine} desc="Silver Line" onClick={() => updateLines(4, "SV")}/>
            <LineSelect selected={selected[5]} className="line" source={YellowLine} desc="Yellow Line" onClick={() => updateLines(5, "YL")}/>
        </div>
        <p>{lines}</p>
        </>
    )
}

export default Input;

