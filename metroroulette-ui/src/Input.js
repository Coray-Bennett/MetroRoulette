import './Input.css';
import { useState, useEffect, useCallback } from 'react';
import LineSelect from './LineSelect';
import RedLine from './images/WMATA_Red.svg';
import GreenLine from './images/WMATA_Green.svg';
import OrangeLine from './images/WMATA_Orange.svg';
import BlueLine from './images/WMATA_Blue.svg';
import SilverLine from './images/WMATA_Silver.svg';
import YellowLine from './images/WMATA_Yellow.svg';
import Route from './Route';

const PATH = 'http://localhost:8080';

const Input = () => {

    const [stationMap, setStationMap] = useState(new Map());
    const [options, setOptions] = useState(null);
    const [stationName, setStationName] = useState("");
    const [selected, setSelected] = useState(Array(6).fill(true));
    const [lines, setLines] = useState(new Set(["RD", "GR", "OR", "BL", "SV", "YL"]));
    const [maxStops, setMaxStops] = useState(0);
    const [maxMinutes, setMaxMinutes] = useState(0);

    const [pathDisplay, setPathDisplay] = useState([]);

    const [showRoute, setShowRoute] = useState(false);

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
            get(PATH + '/MetroRoulette/stations', processStations);
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

    function get(endpoint, parse) {
        fetch(endpoint)
        .then((res) => res.json())
        .then((data) => {
            parse(data);
        })
        .catch((err) => {
            console.log(err.message);
            });
    }

    function parsePathResult(rawPath) {
        console.log("parse");
        console.log(rawPath);
        let result = [];
        let numberStops = 1;

        for(let i in rawPath) {
            i = parseInt(i);
            const station = rawPath[i];
            const lineCodes = station.lineCodes.split(",");

            if(i-1 < 0) {
                result.push(station.name + " " + lineCodes);
                continue;
            }

            if(i+1 > rawPath.length - 1) {
                result.push(numberStops + " STOPS TO " + station.name + " " + lineCodes);
                break;
            }

            const prevStation = rawPath[i-1];
            const prevLineCodes = prevStation.lineCodes.split(",");

            const nextStation = rawPath[i+1];
            const nextLineCodes = nextStation.lineCodes.split(",");

            let transfer = true;

            for(let j in prevLineCodes) {
                j = parseInt(j);
                if (nextLineCodes.includes(prevLineCodes[j])) {
                    transfer = false;
                }
            }

            if(transfer) {
                result.push(numberStops + " STOPS TO " + station.name + " " + lineCodes);
                result.push("TRANSFER " + prevLineCodes + " " + nextLineCodes);
                numberStops = 1;
            }
            else {
                numberStops++;
            }
        }

        setPathDisplay(result);
        setShowRoute(true);
    }

    function handleSubmit() {
        if(stationMap.get(stationName) === undefined) {
            return;
        }
        let stationCode = stationMap.get(stationName).codes.split(",")[0];
        let lines_str = "";
        lines.forEach((line) => lines_str += line + ",")
        lines_str = lines_str.slice(0, -1);

        let request = "";
        request += "?startCode=" + stationCode;
        request += "&maxStops=" + maxStops;
        request += "&selectedLines=" + lines_str;
        request += "&maxLength=" + maxMinutes;

        get(PATH + '/MetroRoulette/' + request, parsePathResult);
    }

    return (
        <>
        <datalist id="stations">
            {options}
        </datalist>
        <div className="station-input">
            <span className="mif-room"></span>
            <input className="input-field" type="search" list="stations" value={stationName}
                placeholder='Starting Station' onChange={(e) => setStationName(e.target.value)}>
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

        <Route path={pathDisplay} visible={showRoute} handleClose={() => setShowRoute(false)}></Route>
        </>
    )
}

export default Input;

