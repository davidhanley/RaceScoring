# -*- coding: utf-8 -*-
# this file is released under public domain and you can use without limitations

#########################################################################
## This is a sample controller
## - index is the default action of any application
## - user is required for authentication and authorization
## - download is for downloading files uploaded in the db (does streaming)
#########################################################################

def index():
    """
    example action using the internationalization operator T and flash
    rendered by views/default/index.html or views/generic.html

    if you need a simple wiki simply replace the two lines below with:
    return auth.wiki()
    """

    categories = [ 'Open' , 
                   '5-9' , 
                   '10-19' , 
                   '20-29' ,
                   '30-39' , 
                   '40-49' , 
                   '50-59' , 
                   '60-69' , 
                   '70-79' , 
                   '80-89' , 
                   '90-99' ] 

    
    f1 = Field('sex', requires=IS_IN_SET(('M','F'),zero=None))
    form = SQLFORM.factory(f1,
                           Field('category', requires=IS_IN_SET(categories,zero=None)) ,
                           Field('name') )
    ds = db.sheets
    fields = [
        ds.name , ds.ranking, ds.age, ds.points, ds.results ]

    chosenSex = 'M'
    chosenCategory = 'Open'
    namesearch = None

    if form.process().accepted:
      chosenSex = form.vars.sex
      f1.default = chosenSex
      chosenCategory = form.vars.category 
      namesearch = form.vars.name
      print namesearch

    q = (db.sheets.sex==chosenSex) & (db.sheets.category == chosenCategory)
    if namesearch <> None:
      q = q & (db.sheets.name.like( '%' + namesearch + '%' ))

    print q 

    grid = SQLFORM.grid( q , fields = fields , search_widget = None )
    return dict(form=form,grid=grid)
    #response.flash = T("Hello World")
    #return dict(message=T('Welcome to web2py!'))


def user():
    """
    exposes:
    http://..../[app]/default/user/login
    http://..../[app]/default/user/logout
    http://..../[app]/default/user/register
    http://..../[app]/default/user/profile
    http://..../[app]/default/user/retrieve_password
    http://..../[app]/default/user/change_password
    http://..../[app]/default/user/manage_users (requires membership in
    http://..../[app]/default/user/bulk_register
    use @auth.requires_login()
        @auth.requires_membership('group name')
        @auth.requires_permission('read','table name',record_id)
    to decorate functions that need access control
    """
    return dict(form=auth())


@cache.action()
def download():
    """
    allows downloading of uploaded files
    http://..../[app]/default/download/[filename]
    """
    return response.download(request, db)


def call():
    """
    exposes services. for example:
    http://..../[app]/default/call/jsonrpc
    decorate with @services.jsonrpc the functions to expose
    supports xml, json, xmlrpc, jsonrpc, amfrpc, rss, csv
    """
    return service()


