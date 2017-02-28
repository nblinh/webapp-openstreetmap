angular.module("generationService", ['ngResource'])
.factory('getProjection', function($resource){
    return $resource('rest/getProjection',{},{
        charge: {'method':'GET'}
    });
})
.factory('generationProjectionCalibration', function($resource){
    return $resource('rest/generationProjectionCalibration',{},{
        charge: {'method':'GET'}
    });
})
;