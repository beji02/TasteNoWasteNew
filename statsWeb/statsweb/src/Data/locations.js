import DataReader from "./dataReader";

const dataValues = [
    {coordinates: {lat: 45.7776 , lng: 21.2222}, plasticCount: 0, restCount: 0, paperCount: 0},
    {coordinates: {lat: 45.7726 , lng: 21.2414}, plasticCount: 0, restCount: 0, paperCount: 0},
    {coordinates: {lat: 45.7618 , lng: 21.2362}, plasticCount: 0, restCount: 0, paperCount: 0},
    {coordinates: {lat: 45.7605 , lng: 21.2552}, plasticCount: 0, restCount: 0, paperCount: 0},
    {coordinates: {lat: 45.75   , lng: 21.2431}, plasticCount: 0, restCount: 0, paperCount: 0},
    {coordinates: {lat: 45.7415 , lng: 21.2691}, plasticCount: 0, restCount: 0, paperCount: 0},
    {coordinates: {lat: 45.7246 , lng: 21.2681}, plasticCount: 0, restCount: 0, paperCount: 0},
    {coordinates: {lat: 45.7341 , lng: 21.2494}, plasticCount: 0, restCount: 0, paperCount: 0},
    {coordinates: {lat: 45.7427 , lng: 21.2453}, plasticCount: 0, restCount: 0, paperCount: 0},
    {coordinates: {lat: 45.7317 , lng: 21.2346}, plasticCount: 0, restCount: 0, paperCount: 0},
    {coordinates: {lat: 45.7423 , lng: 21.2201}, plasticCount: 0, restCount: 0, paperCount: 0},
    {coordinates: {lat: 45.7201 , lng: 21.2078}, plasticCount: 0, restCount: 0, paperCount: 0},
    {coordinates: {lat: 45.7319 , lng: 21.2125}, plasticCount: 0, restCount: 0, paperCount: 0},
    {coordinates: {lat: 45.7436 , lng: 21.2022}, plasticCount: 0, restCount: 0, paperCount: 0},
    {coordinates: {lat: 45.7571 , lng: 21.1864}, plasticCount: 0, restCount: 0, paperCount: 0},
    {coordinates: {lat: 45.7556 , lng: 21.2073}, plasticCount: 0, restCount: 0, paperCount: 0},
    {coordinates: {lat: 45.7646 , lng: 21.2017}, plasticCount: 0, restCount: 0, paperCount: 0},
    {coordinates: {lat: 45.7625 , lng: 21.2175}, plasticCount: 0, restCount: 0, paperCount: 0},
    {coordinates: {lat: 45.7556 , lng: 21.2275}, plasticCount: 0, restCount: 0, paperCount: 0}
]

export default class LocationComputations {
    constructor (dataReader){
        this.dataReader = dataReader
    }

    computeDistance = (locationPackagePair, locationCenter) => {
        return 1000 * (locationCenter.coordinates.lat - locationPackagePair.coordinates.latitude ) * (locationCenter.coordinates.lat - locationPackagePair.coordinates.latitude) +
        1000 *  (locationCenter.coordinates.lng - locationPackagePair.coordinates.longitude ) * (locationCenter.coordinates.lng - locationPackagePair.coordinates.longitude)
    }
    
    calculateProportions = () => {
        const locationsPackagesList = this.dataReader.getBruteLocationPackagesList()
    
        for (const locationPackagePair of locationsPackagesList) {
            let minimumDistance = this.computeDistance(locationPackagePair, dataValues[0])
            let minimumIndex = 0
            

            for (let i=0; i < dataValues.length; ++i){
                let currentDistance = this.computeDistance(locationPackagePair, dataValues[i])
                console.log(currentDistance)
                if (currentDistance < minimumDistance) {
                    minimumIndex = i
                    minimumDistance = currentDistance
                }
            }

            for (const packageType of locationPackagePair.packages) {
                if (packageType.includes("Plast") || packageType.includes("plast")) dataValues[minimumIndex].plasticCount++;
                else if (packageType == "Paper") dataValues[minimumIndex].paperCount++;
                else dataValues[minimumIndex].restCount++;
            }
        }
    
        return dataValues
    }
    
}

