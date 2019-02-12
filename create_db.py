#!/usr/bin/python

# Program to generate scores from race results sheets
# format: Bib # , Name , age , gender , time 

import sys
import os
import csv
import math
import sqlite3
import common
import datetime

#try:
#  os.remove( 'results.db' )
#except:
#  pass
conn = sqlite3.connect(common.db)
c = conn.cursor()

def saferun(cmd):
  try:
    c.execute(cmd)
  except:
    print "failed to run:" , cmd
    
saferun( "create table race    ( id INTEGER PRIMARY KEY AUTOINCREMENT  , name string , date date , factor integer , url string )" )
saferun( "create table athlete ( id INTEGER PRIMARY KEY AUTOINCREMENT  , name string , sex string , age integer , points float )" )
saferun( "create index athname on athlete(name)" ) # cut creation time from 50.8 to 30.8
saferun( "create table results  ( id INTEGER PRIMARY KEY AUTOINCREMENT  , race integer , athlete integer , rank integer , points float )" )
saferun( "create index resath on results(athlete)" ) #cut creation time from 30.8 to 1.26 (!!) 
saferun( "create table sheets ( id INTEGER PRIMARY KEY AUTOINCREMENT, name string, sex string, age integer, category string, ranking integer, points float, results string ) ") 


c.execute( "delete from race where 1=1" )
c.execute( "delete from athlete where 1=1" )
c.execute( "delete from results where 1=1" )
c.execute( "delete from sheets where 1=1" )


translations = open( 'translate.dat' , 'r' ).readlines()
translations = [ t.strip().upper().split(',') for t in translations ]
print translations 

def translate( name ):
  name = name.upper()
  for t in translations:
    if t[0]==name:
      print "translating", t,t[1]
      return t[1]
  return name

def find_racer( name , age , gender ):
  name = translate(name)
  try:
    age = int( age )
    c.execute( "select id , age from athlete where name=? and sex=? and ( ( age >= ? ) and ( age <= ? ) or age is null )" , 
               ( name , gender , age - 2 , age + 2 ) ) 
  except:
    c.execute( "select id , age from athlete where name=? and sex=?" , ( name , gender  ) )
  try:
    row = c.fetchone()
    if isinstance( age , int ): 
      if row[ 1 ] == None or age>row[1]:
        c.execute( "update athlete set age = ? where id = ?" , ( age , row[ 0 ] ) )
    return row[ 0 ]
  except:
    return None

def try_add( name , age , gender ):
  id = find_racer( name , age , gender )
  if id <> None:
    return id
  try:
    age = int( age )
    c.execute( "insert into athlete( name , sex , age ) values( ? , ? , ? )" , ( name , gender , age ) )
  except:
    c.execute( "insert into athlete( name , sex ) values( ? , ? )" , ( name , gender ) )
  return c.lastrowid

 
def add_gender( race_id , racers ):
  rank = 1 
  seen = {}
  for racer in racers:
    if not seen.has_key( racer ):
      c.execute( "insert into results(  race , athlete , rank ) values(?,?,?)" , ( race_id , racer , rank ) )
      rank = rank + 1 
      seen[ racer ] = 1
    else:
      pass
      #print "Skipping" , race_id , racer , rank
   
def fixName( name ):
   cs = name.split( "," )
   if len( cs ) > 1:
     name = cs[1]+" "+cs[0]
   name = " ".join( name.split() )
   return name


def nastrip(str):
  return ''.join(i for i in str if ord(i)<128)

def main():
  sheets = os.popen( "ls -t data/*.csv" ).readlines()

  for sheet in sheets:
    sheet = sheet.strip()
    with open( sheet ) as results_file:
      print >> sys.stderr , "processing" , sheet 
      def pop():
        l = results_file.readline()
        #print(l)
        l = l.split("#")[0]
        l = l.strip().split(",")
        return l[0]

      event = pop()

      dp = [ int(x) for x in pop().strip().split('-') ]
      date = datetime.date( dp[0] , dp[1], dp[2]  )

      pseudoNow = datetime.date.today()
      #pseudoNow = datetime.date( 2018 , 12 , 31) 
      if date > pseudoNow or date < pseudoNow - datetime.timedelta(365):
        print "******event is over a year old, skipping" 
        continue 
      #so much ugly code, especially this:
      fs = pop()
      url = fs
      fs = pop()
      factor = int( fs )

      c.execute( "insert into race(name,factor,date,url) values(?,?,?,?)" , ( event , factor , date , url ) )
      race_id = c.lastrowid

      male_race   = []
      female_race = [] 
      result_reader = csv.reader( results_file , delimiter = ',' , quotechar = '"' )
      for result in result_reader:
        result = [unicode(cell, 'utf-8') for cell in result]
        (name,age,gender,time) = [ nastrip( result[a].strip().upper()) for a in (1,2,3,-1) ]
        
        name = fixName( name ) 
        gender = gender.upper()
        gender=gender.strip()
        if len(gender)>1:
          gender = gender[:1]
        athlete_id = try_add( name , age , gender )
        age = str(age)
        #print name , gender , time 
        gender = gender.upper()
        if gender == 'M':
          male_race.append( athlete_id )
        elif gender == 'F':
          female_race.append( athlete_id )  
        else: 
          pass 
      add_gender( race_id , male_race )
      add_gender( race_id , female_race )

main()
conn.commit()
