<script id="defaultDeleteDialogTpl" type="text/template">
    <div class="modal hide fade" id="{{= modalId }}">
        <div class="modal-header">
            <a href="#" class="close" data-dismiss="modal">&times;</a>
            <h3><g:message code="ui.label.deleteConfirm"/></h3>
        </div>
        <div class="modal-body">
            <p><g:message code="ui.label.deleteConfirmBody"/></p>
        </div>
        <div class="modal-footer">
            <a href="#" class="btn btn-danger delete-btn-confirm"><g:message code="ui.button.delete"/></a>
        </div>
    </div>
</script>