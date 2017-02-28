angular.module("zoningService", ['ngResource'])
.factory('getZoning', function($resource){
    return $resource('rest/getZoning',{},{
        charge: {'method':'GET'}
    });
})
.factory('getZonningZonalData', function($resource){
    return $resource('rest/getZonningZonalData',{},{
        charge: {'method':'GET'}
    });
});