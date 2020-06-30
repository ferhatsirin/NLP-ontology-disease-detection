from gensim.models.doc2vec import Doc2Vec
import gensim
import sys
import collections


# başarı oranını hesaplar
def calculateRank(train_corpus,model):
    ranks =[];
    second_ranks =[];
    for doc_id in range(len(train_corpus)):
        inferred_vector =model.infer_vector(train_corpus[doc_id].words);
        sims =model.docvecs.most_similar([inferred_vector],topn=len(model.docvecs));
        rank =[docid for docid, sim in sims].index(doc_id);
        ranks.append(rank)
        second_ranks.append(sims[1]);

    t =collections.Counter(ranks);
    print(t);


# en benzer n tane vektörü döndürür.
def getMostSimilar(doc, model,n):
    test =gensim.utils.simple_preprocess(doc);
    vect =model.infer_vector(test);
    similarity =model.docvecs.most_similar([vect],topn=n);
    return similarity;


if __name__ == "__main__":

    modelFiles ="dataset/models/";

    datasetModule =Doc2Vec.load(modelFiles+"dataset.model");

    while True :
        line =sys.stdin.readline();
        line =line.replace("\n","");

        if line == "DATASET":
            line =sys.stdin.readline();
            line =line.replace("\n","");
            articleList =getMostSimilar(line,datasetModule,len(datasetModule.docvecs));
            print(len(articleList));
            for i in articleList:
                print(i[0]);
                print(i[1]);
            sys.stdout.flush();

        elif line == "ARTICLE":
            modelName =sys.stdin.readline();
            line =sys.stdin.readline();
            modelName =modelName.replace("\n","");
            line =line.replace("\n","");
            model =Doc2Vec.load(modelFiles+modelName+".model");

            paragraphList =getMostSimilar(line,model,10);
            print(len(paragraphList));
            for i in paragraphList:
                print(i[0]);
                print(i[1]);
            sys.stdout.flush();

        else:
            break;
