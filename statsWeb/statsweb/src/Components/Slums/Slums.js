import React from 'react';
import MapSlums from './MapSlums';


export default class Slums extends React.Component{
    constructor(props){
        super(props);
        this.state = {
            zoom: 0,
            lat: 0,
            lng: 0
        }
    }

handleMapViewChange = (zoom, lat, lng) => {
    this.setState({
      lat,
      lng,
      zoom
    })
  }


  render() {
    const {
      zoom,
      lat,
      lng
    } = this.state;
    return (
      <div className="Slums">
        <MapSlums
          lat={lat}
          lng={lng}
          onMapViewChange={this.handleMapViewChange}
          zoom={zoom}
        />
      </div>
    );
  }
}
