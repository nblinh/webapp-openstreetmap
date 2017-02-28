angular.module("menuService", ['ngResource'])
.factory('getMenu', function($resource){
    return $resource('rest/getMenu',{},{
        charge: {'method':'GET'}
    });
})
.factory('getGenerationProjectionInfo', function($resource){
    return $resource('rest/getGenerationProjectionInfo',{},{
        charge: {'method':'GET'}
    });
})
.factory('getProjectionODMatrixInfo', function($resource){
    return $resource('rest/getProjectionODMatrixInfo',{},{
        charge: {'method':'GET'}
    });
})
.factory('shareObject', function(){
    return {};
})
;