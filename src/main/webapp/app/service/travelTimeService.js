angular.module("travelTimeService", ['ngResource'])
.factory('getTravelTime', function($resource){
    return $resource('rest/getTravelTime',{},{
        charge: {'method':'GET'}
    });
})
;