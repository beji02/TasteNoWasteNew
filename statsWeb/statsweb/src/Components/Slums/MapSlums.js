import React from 'react';
import H from "@here/maps-api-for-javascript";
import ui from "@here/maps-api-for-javascript";
import onResize from 'simple-element-resize-detector';
import {neighborhoods} from '../../Data/Neighborhoods.js'
import DataReader from '../../Data/dataReader.js';
import LocationComputations from '../../Data/locations.js';


export default class MapSlums extends React.Component {
    
  constructor(props) {
    super(props);
    // the reference to the container
    this.ref = React.createRef();
    // reference to the map
    this.map = null;
    this.ui = null;
    const dataReader = new DataReader()
    const locationPackagesList = dataReader.getLocationPackagesList()
    const promise1 = new Promise((resolve, reject) => {
      resolve('Success!');
      console.log(locationPackagesList);
    });
    promise1.then((locationPackagesList) => {
      console.log(locationPackagesList);
    });

  }

  addMarkersToMap(map) {
    var steauaFratelia = new H.map.Marker({lat: 45.72129069193045, lng: 21.207864485222203});
    map.addObject(steauaFratelia);
    steauaFratelia.setVisibility(false)
  }

  addPolygon(map, coordinates) {
    var lineString = new H.geo.LineString(
      coordinates,
      'values lat lng alt'
    );
    map.addObject(
      new H.map.Polygon(lineString, {
        style: {
          fillColor: "rgba(250, 200, 100, 0.5)",
          strokeColor: '#829',
          lineWidth: 4
        }
      })
    );
  }

  componentDidMount() {
    if (!this.map) {
      const platform = new H.service.Platform({
        apikey: 'Uw03yhTzLtMNLP5KsdB5Q1nMh0VeTb2Ddi40fgFFGqo'
      });
      const layers = platform.createDefaultLayers();
      
      const map = new H.Map(
        this.ref.current,
        layers.vector.normal.map,
        {
          pixelRatio: window.devicePixelRatio,
          center: {lat: 45.756698, lng: 21.228805},
          zoom: 13,
        },
      );
      onResize(this.ref.current, () => {
        map.getViewPort().resize();
      });
      this.map = map;
      
      
      // attach the listener
      map.addEventListener('mapviewchange', this.handleMapViewChange);
      window.addEventListener('resize', () => this.map.getViewPort().resize());
      this.restrictMap(this.map);

      for(let neighborhood in neighborhoods) {
        var Neighborhood = neighborhoods[neighborhood]; 
        this.addPolygon(this.map, Neighborhood);
      }
      this.addInfoBubble(map, layers)
      this.addMarkersToMap(map);
      // add the interactive behaviour to the map
      new H.mapevents.Behavior(new H.mapevents.MapEvents(map));
    }
  }

  componentDidUpdate() {
    const {
      lat,
      lng,
      zoom
    } = this.props;

    if (this.map) {
      // prevent the unnecessary map updates by debouncing the
      // setZoom and setCenter calls
      this.setUpClickListener(this.map)

      clearTimeout(this.timeout);
      this.timeout = setTimeout(() => {
        this.map.setZoom(zoom);
        this.map.setCenter({lat, lng});
      }, 100);

    }
  }
    
    setUpClickListener(map) {
    // Attach an event listener to map display
    // obtain the coordinates and display in an alert box.
    map.addEventListener('tap', function (evt) {
      var coord = map.screenToGeo(evt.currentPointer.viewportX,
              evt.currentPointer.viewportY);
      console.log('Clicked at ' + Math.abs(coord.lat.toFixed(4)) +
          ((coord.lat > 0) ? 'N' : 'S') +
          ' ' + Math.abs(coord.lng.toFixed(4)) +
           ((coord.lng > 0) ? 'E' : 'W'));
      });
    }

     addMarkerToGroup(group, coordinate, html) {
      var marker = new H.map.Marker(coordinate);
      // add custom data to the marker
      marker.setData(html);
      group.addObject(marker);
    }
    
     addInfoBubble(map, layers) {
      var group = new H.map.Group();
    
      map.addObject(group);
    
      // add 'tap' event listener, that opens info bubble, to the group
      group.addEventListener('tap', function (evt) {
        // event target is the marker itself, group is a parent event target
        // for all objects that it contains
        var bubble = new H.ui.InfoBubble(evt.target.getGeometry(), {
          // read custom data
          content: evt.target.getData()
        });
        // show info bubble

        H.ui.UI.createDefault(map, layers).addBubble(bubble);
      }, false);
      
        let locationComputations = new LocationComputations()
        let proportions = locationComputations.calculateProportions()


        for (const markerValue of proportions){
          let totalPackages = markerValue.plasticCount + markerValue.restCount + markerValue.paperCount
          let plasticProp = 0 
          let paperProp = 0 
          let restProp = 0
          
          if (totalPackages) {
            plasticProp = markerValue.plasticCount / totalPackages
            paperProp = markerValue.paperCount / totalPackages
            restProp = markerValue.restCount / totalPackages
          }
          
          this.addMarkerToGroup(group, markerValue.coordinates, 
            '<div> <p font_weight="bold">Waste proportions:</p></div>' +
            `<div>${plasticProp}% plastic.<br />${restProp}% rest.<br /> ${paperProp}% paper.</div>`);
      }
    }

  handleMapViewChange = (ev) => {
    const {
      onMapViewChange
    } = this.props;
    if (ev.newValue && ev.newValue.lookAt) {
      const lookAt = ev.newValue.lookAt;
      // adjust precision
      const lat = Math.trunc(lookAt.position.lat * 1E7) / 1E7;
      const lng = Math.trunc(lookAt.position.lng * 1E7) / 1E7;
      const zoom = Math.trunc(lookAt.zoom * 1E2) / 1E2;
      onMapViewChange(zoom, lat, lng);
    }
    
  }

  restrictMap = (map) => {
    if(map != null) {
      const bounds = new H.geo.Rect(45.806698, 21.178805, 45.706698, 21.278805);

      map.getViewModel().addEventListener('sync', function() {
        let center = map.getCenter();

        if (!bounds.containsPoint(center)) {
          if (center.lat > bounds.getTop()) {
            center.lat = bounds.getTop();
          } else if (center.lat < bounds.getBottom()) {
            center.lat = bounds.getBottom();
          }
          if (center.lng < bounds.getLeft()) {
            center.lng = bounds.getLeft();
          } else if (center.lng > bounds.getRight()) {
            center.lng = bounds.getRight();
          }
          map.setCenter(center);
        }

        let zoom = map.getZoom();

        if(zoom < 12) {
          zoom = 12;
          map.setZoom(zoom);
        }
      });
    }
}
  render() {
    return (
      <div
        style={{ position: 'relative', width: '100%', height:'85vh' }}
        ref={this.ref}
      />
    )
  }

  
}