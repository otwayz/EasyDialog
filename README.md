The dialog is supported wrap content and match parent 

even the content width to screen width ratio.

the same to height.


you can use like 


```
    easyDialog = new EasyDialog.Builder(this)
                    .setContentViewId(R.layout.dialog_ugc_finish_read)
                    .setWidth(-2)
                    .setHeight(EasyDialog.WRAP_CONTENT)
                    .setBackgroundColor(Color.CYAN)
                    .setOnClickListener(R.id.id_iv_close, this)
                    .setOnClickListener(R.id.id_btn_checkin, this)
                    .show()
```

support click , long click and check event.

you can design your self if you like.

have fun. Thanks!


