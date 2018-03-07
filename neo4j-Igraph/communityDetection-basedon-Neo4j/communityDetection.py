from py2neo import Graph
from igraph import Graph as ig


##Connect neo4j and create the graph object
graph = Graph("http://localhost:7474/db/data/", username="**", password="**")
query = '''
MATCH (c1:Character)-[r:INTERACTS]->(c2:Character)
RETURN c1.name, c2.name, r.weight AS weight
'''
communitydata = ig.TupleList(graph.run(query), weights=True)

##Pagerank Algorithm
pg = communitydata.pagerank()
print pg
pgvs = []
for p in zip(communitydata.vs, pg):
    print(p)
    pgvs.append({"name": p[0]["name"], "pg": p[1]})
pgvs
write_clusters_query = '''
UNWIND {nodes} AS n
MATCH (c:Character) WHERE c.name = n.name
SET c.pga = n.pg
'''
graph.run(write_clusters_query, nodes=pgvs)
# communitydata = ig.Read_Ncol("C:\\Users\\Administrator\\Desktop\\CommunityDetection\\testCommunity_D.csv",names=True,weights=True,directed=False)
#implement = communitydata.community_multilevel()
#print(implement)
# implement1 = communitydata.community_walktrap()
# OptimalQ=communitydata.modularity(implement)
#OptimalQ1 = communitydata.modularity(implement1)
# print implement1

##walktrap Algorithm
clusters = ig.community_walktrap(communitydata, weights="weight").as_clustering()
for node in communitydata.vs:
    print node
nodes = [{"name": node["name"]} for node in communitydata.vs]
print nodes
for node in nodes:
    idx = communitydata.vs.find(name=node["name"]).index
    node["community"] = clusters.membership[idx]
    print node
write_clusters_query = '''
UNWIND {nodes} AS n
MATCH (c:Character) WHERE c.name = n.name
SET c.community = toInt(n.community)
'''
graph.run(write_clusters_query, nodes=nodes)

#Fast unfolding Algorithm
FU_clusters = ig.community_multilevel(communitydata, weights="weight")
for node in communitydata.vs:
    print node
nodes = [{"name": node["name"]} for node in communitydata.vs]
print nodes
for node in nodes:
    idx = communitydata.vs.find(name=node["name"]).index
    node["FU_community"] = FU_clusters.membership[idx]
    print node
write_clusters_query = '''
UNWIND {nodes} AS n
MATCH (c:Character) WHERE c.name = n.name
SET c.FU_community = toInt(n.FU_community)
'''
graph.run(write_clusters_query, nodes=nodes)
