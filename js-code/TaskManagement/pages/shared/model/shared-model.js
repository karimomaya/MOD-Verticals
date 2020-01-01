function SharedModel(){
    return {
        getUserUnderUnitsByUnitCodes : getUserUnderUnitsByUnitCodes
    }

    function getUserUnderUnitsByUnitCodes(unitCodes, userUnitId, input){
        let obj={
            method: "MOD_SYS_GENERALMOD_SP_getUserUnderUnitsByUnitCodes",
            namespace: "http://schemas.cordys.com/MOD_SYS_GENERALMOD_SP_getUserUnderUnitsByUnitCodes",
            param : {
                RETURN_VALUE:"RETURN_VALUE",
                UnitCodes: unitCodes,
                userUnitId:userUnitId,
                PageNumber: 1,
                input:input,
                PageSize: assetConfig.autocompleteSize
            }
          };
          return Cordys().cordysAjax(obj.method,obj.namespace,obj.param);
    }
}