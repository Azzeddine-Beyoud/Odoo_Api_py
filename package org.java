package org.example;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;

public class thread {

    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {

            final XmlRpcClientConfigImpl common_config = new XmlRpcClientConfigImpl();
            final String url = "http://10.1.0.32:8015",
                    db = "odoo_production",
                    username = "admin",
                    password = "$$tapidor!!";

            final XmlRpcClient models = new XmlRpcClient() {{
                setConfig(new XmlRpcClientConfigImpl() {{
                    try {

                        setServerURL(new URL(String.format("%s/xmlrpc/2/object", url)));
                        common_config.setServerURL(new URL(String.format("%s/xmlrpc/2/common", url)));

                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                }});
            }};

            try {
                int uid = (int) models.execute(common_config, "authenticate", asList(db, username, password, emptyMap()));

                final List<String> list_chariots = chariot_info(getChariottList(models, db, uid, password));

                auto_complete_n_chariot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                list_chariot_out_thread.addAll(list_chariots);
                                // set custom dialog
                                dialog.setContentView(R.layout.dialog_searchable_spinner);

                                // set custom height and width
                                dialog.getWindow().setLayout(1200, 3000);

                                // set transparent background
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                // show dialog
                                dialog.show();
                                // Initialize and assign variable
//                AutoCompleteTextView search_chriot = dialog.findViewById(R.id.eText_nbr_Tapis);


                                // Initialize array adapter
                                adapter = new ArrayAdapter<>(NewChariot.this, android.R.layout.simple_list_item_1, list_chariot_out_thread);
                                listView = dialog.findViewById(R.id.list_view);

                                // set adapter
                                listView.setAdapter(adapter);

                                editText.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        adapter.getFilter().filter(s);
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                    }
                                });

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        // when item selected from list
                                        // set selected item on textView
                                        auto_complete_n_chariot.setText(adapter.getItem(position));

                                        // Dismiss dialog
                                        dialog.dismiss();
                                    }
                                });
                            }
                        });
                    }
                });

            } catch (XmlRpcException e) {
                throw new RuntimeException(e);
            }

        }
    });
        thread.start();
}
