import xmlrpc.client
import ssl

url= 'http://10.1.0.32:8015'
db = 'odoo_production'
username = 'admin'
password = '$$tapidor!!'

common = xmlrpc.client.ServerProxy('{}/xmlrpc/2/common'.format(url))
print(common)

uid = common.authenticate(db, username, password, {})

if uid:
    print("authentication success")
else:
    print("authentication failed")


models = xmlrpc.client.ServerProxy('{}/xmlrpc/2/object'.format(url))
partners = models.execute_kw(db, uid, password, 'res.partner', 'search', [[['is_company', '=', True]]])

print("----->", len(partners))

ids = models.execute_kw(db, uid, password, 'quality.control.order', 'search', [[]], {'limit': 1})
visit = models.execute_kw(db, uid, password, 'quality.control.order', 'read', [ids], {'fields': ['visiting_id']})

print("----->", visit )