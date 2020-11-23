const axios = require('axios');
const fs = require('fs');

//headers for api req (now.vn)
const headersForNowReq = {
    'x-foody-api-version': '1',
    'x-foody-client-id': '',
    'x-foody-client-type': '1',
    'x-foody-app-type': '1004',
    'x-foody-client-version': '1',
}

getData = async(ids, fileName) => {
    console.log("Starting clone data...")

    fs.writeFileSync(fileName, '', 'utf-8');
    fs.appendFileSync(fileName, '[', 'utf-8');

    const resInfos = await axios.post('https://gappapi.deliverynow.vn/api/delivery/get_infos', {
        restaurant_ids: ids
    }, {
        headers: headersForNowReq
    })

    let data = resInfos.data.reply.delivery_infos;
    let menu_infos;
    for (let i = 0; i < data.length; i++) {
        let id = data[i].id;
        const resMenu = await axios.get('https://gappapi.deliverynow.vn/api/dish/get_delivery_dishes', {
            headers: headersForNowReq,
            params: {
                id_type: 2,
                request_id: id
            }
        });
        menu_infos = resMenu.data.reply.menu_infos;
        data[i].menu_infos = menu_infos;
        delete data[i].position.is_verified;

        const resDetail = await axios.get('https://gappapi.deliverynow.vn/api/delivery/get_detail', {
            headers: headersForNowReq,
            params: {
                id_type: 2,
                request_id: id
            }
        });

        data[i].price_range = resDetail.data.reply.delivery_detail.price_range;
        await fs.appendFileSync(fileName, JSON.stringify(data[i]), 'utf-8');
        if (i < data.length - 1) {
            await fs.appendFileSync(fileName, ', ', 'utf-8');
        }
    }
    await fs.appendFileSync(fileName, ']', 'utf-8');
    console.log('Success!');
}


const ids = [712005, 1024034, 734378, 692259, 728011, 757262, 723995, 1012779, 969879, 899520, 1049727, 1047465, 1053510, 971483, 1046479, 1035447, 1056208, 1022929, 1047511, 1056075, 1056097, 1049592, 851518, 794330, 797376]

getData(ids, './resources/raw-data/test.json');