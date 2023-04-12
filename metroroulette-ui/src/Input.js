import './Input.css';
import { useState, useEffect } from 'react';
import LineSelect from './LineSelect';
import RedLine from './images/WMATA_Red.svg';
import GreenLine from './images/WMATA_Green.svg';
import OrangeLine from './images/WMATA_Orange.svg';
import BlueLine from './images/WMATA_Blue.svg';
import SilverLine from './images/WMATA_Silver.svg';
import YellowLine from './images/WMATA_Yellow.svg';


const Input = () => {

    const [stations, setStations] = useState(null);
    const [stationCode, setStationCode] = useState("");
    const [selected, setSelected] = useState(Array(6).fill(true));
    const [lines, setLines] = useState(new Set(["RD", "GR", "OR", "BL", "SV", "YL"]));
    
    useEffect( () =>  {
        let tried = false;
        if(stations == null && !tried) {
            get('http://localhost:8080/MetroRoulette/stations');
            tried = true;
        }
    });

    function updateLines(index, value) {
        if(lines.size === 1 && lines.has(value)) {return;}

        let newSelected = selected.slice();
        newSelected[index] = !newSelected[index];
        setSelected(newSelected);

        let newLines = new Set(lines);
        if(newLines.has(value)) {
            newLines.delete(value);
        }
        else {
            newLines.add(value);
        }
        setLines(newLines);
    }

    function get(endpoint) {
        fetch(endpoint)
        .then((res) => res.json())
        .then((data) => {
            setStations(data);
            console.log(data);
        })
        .catch((err) => {
            console.log(err.message);
            });
    }

    function handleSubmit() {
        //get('http://localhost:8080/MetroRoulette/stations')
        console.log(stations[0]);
    }

    return (
        <>
        <datalist id="stations"></datalist>
        <div className="station-input">
            <span className="mif-room"></span>
            <input className="input-field" type="search" list="stations" value={stationCode}
                onChange={(e) => setStationCode(e.target.value)}>
            </input>
        </div>
        
        <div className="lines-input noselect">
            <LineSelect selected={selected[0]} className="line" source={RedLine} desc="Red Line" onClick={() => updateLines(0, "RD")}/>
            <LineSelect selected={selected[1]} className="line" source={GreenLine} desc="Green Line" onClick={() => updateLines(1, "GR")}/>
            <LineSelect selected={selected[2]} className="line" source={OrangeLine} desc="Orange Line" onClick={() => updateLines(2, "OR")}/>
            <LineSelect selected={selected[3]} className="line" source={BlueLine} desc="Blue Line" onClick={() => updateLines(3, "BL")}/>
            <LineSelect selected={selected[4]} className="line" source={SilverLine} desc="Silver Line" onClick={() => updateLines(4, "SV")}/>
            <LineSelect selected={selected[5]} className="line" source={YellowLine} desc="Yellow Line" onClick={() => updateLines(5, "YL")}/>
        </div>

        <div className="max-input">
            <div className="sub-input">
            <span className="mif-subway"></span>
            <input type="number" className="input-field"></input>
            </div>
            <div className="sub-input">
            <span className="mif-watch"></span>
            <input type="number" className="input-field"></input>
            </div>
            
        </div>

        <button className="submit-button" onClick={handleSubmit}>
            <span className="mif-dice"></span>
        </button>
        </>
    )
}

export default Input;

