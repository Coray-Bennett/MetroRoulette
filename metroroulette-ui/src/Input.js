import './Input.css';
import { useState, useEffect, useCallback } from 'react';
import LineSelect from './LineSelect';
import RedLine from './images/WMATA_Red.svg';
import GreenLine from './images/WMATA_Green.svg';
import OrangeLine from './images/WMATA_Orange.svg';
import BlueLine from './images/WMATA_Blue.svg';
import SilverLine from './images/WMATA_Silver.svg';
import YellowLine from './images/WMATA_Yellow.svg';


const Input = () => {

    const [stationMap, setStationMap] = useState(new Map());
    const [options, setOptions] = useState(null);
    const [stationCode, setStationCode] = useState("");
    const [selected, setSelected] = useState(Array(6).fill(true));
    const [lines, setLines] = useState(new Set(["RD", "GR", "OR", "BL", "SV", "YL"]));
    const [maxStops, setMaxStops] = useState(0);
    const [maxMinutes, setMaxMinutes] = useState(0);

    const [pathResult, setPathResult] = useState(null);

    const processStations = useCallback( (stations) => {
        let stationsSet = new Set();
        let newStationMap = new Map();

        for(let index in stations) {
            stationsSet.add(stations[index].name);
            newStationMap.set(stations[index].name, stations[index]);
        }

        let newOptions = [...stationsSet].map((name) =>
           <option key={name} value={name}></option>);
        setOptions(newOptions);
        setStationMap(newStationMap);
    }, []);
    
    useEffect( () =>  {
        let tried = false;
        if(!tried) {
            get('http://localhost:8080/MetroRoulette/stations', processStations);
            tried = true;
        }
    }, [processStations]);

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

    function get(endpoint, set) {
        fetch(endpoint)
        .then((res) => res.json())
        .then((data) => {
            set(data);
        })
        .catch((err) => {
            console.log(err.message);
            });
    }

    function handleSubmit() {
        console.log(stationMap.get(stationCode));
        console.log(lines);
        console.log(maxStops);
        console.log(maxMinutes);
    }

    return (
        <>
        <datalist id="stations">
            {options}
        </datalist>
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
            <input type="number" className="input-field" value={maxStops} onChange={(e) => setMaxStops(e.target.value)}></input>
            </div>
            <div className="sub-input">
            <span className="mif-watch"></span>
            <input type="number" className="input-field" value={maxMinutes} onChange={(e) => setMaxMinutes(e.target.value)}></input>
            </div>
            
        </div>

        <button className="submit-button" onClick={handleSubmit}>
            <span className="mif-dice"></span>
        </button>
        </>
    )
}

export default Input;

